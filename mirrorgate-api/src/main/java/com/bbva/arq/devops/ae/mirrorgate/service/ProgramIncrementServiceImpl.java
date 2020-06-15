package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramIncrementServiceImpl implements ProgramIncrementService {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramIncrementServiceImpl.class);

    private final IssueService issueService;
    private final DashboardService dashboardService;

    @Autowired
    public ProgramIncrementServiceImpl(final IssueService issueService, final DashboardService dashboardService) {
        this.dashboardService = dashboardService;
        this.issueService = issueService;
    }

    @Override
    public ProgramIncrementDTO getProgramIncrement(final String dashboardName) {
        LOG.debug("Getting product increment information for dashboard : {}", dashboardName);

        final DashboardDTO dashboard = dashboardService.getDashboard(dashboardName);

        final ProgramIncrementDTO piDTO = getCurrentProgramIncrementNameAndDates(dashboard);

        if (piDTO == null) {
            return new ProgramIncrementDTO();
        }

        return createResponse(dashboard, piDTO)
            .setProgramIncrementName(piDTO.getProgramIncrementName())
            .setProgramIncrementStartDate(piDTO.getProgramIncrementStartDate())
            .setProgramIncrementEndDate(piDTO.getProgramIncrementEndDate());

    }

    private ProgramIncrementDTO getCurrentProgramIncrementNameAndDates(final DashboardDTO dashboard) {
        return getProductIncrementNameAndDatesForExpression(dashboard.getProgramIncrement());
    }

    ProgramIncrementDTO getProductIncrementNameAndDatesForExpression(final String productIncrementExpression) {

        final Pattern piRegex = Pattern.compile("^" + productIncrementExpression + "$");

        final ProgramIncrementNamesAggregationResult result = issueService.getProductIncrementFromPiPattern(piRegex);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        List<String> piNames = null;
        if (result != null) {
            piNames = result.getPiNames();
        }

        if (piNames != null) {

            piNames = piNames.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

            for (final String piName : piNames) {

                final Matcher matcher = piRegex.matcher(piName);

                while (matcher.find()) {
                    if (matcher.groupCount() > 1) {
                        //get groups
                        final String startDate = matcher.group("startDate");
                        final String endDate = matcher.group("endDate");

                        if (findIfLocalDateIsInRange(startDate, endDate)) {

                            Date programIncrementStartDate = null;
                            Date programIncrementEndDate = null;

                            try {
                                programIncrementStartDate = formatter.parse(startDate);
                                programIncrementEndDate = formatter.parse(endDate);

                            } catch (ParseException e) {
                                LOG.error("Parse exception", e);
                            }

                            return new ProgramIncrementDTO()
                                .setProgramIncrementName(piName)
                                .setProgramIncrementStartDate(programIncrementStartDate)
                                .setProgramIncrementEndDate(programIncrementEndDate);

                        }
                    } else {
                        return new ProgramIncrementDTO().setProgramIncrementName(matcher.group());
                    }
                }
            }
        }
        return null;
    }

    private ProgramIncrementDTO createResponse(final DashboardDTO dashboard, final ProgramIncrementDTO piDTO) {

        // Get features belonging to this board and this PI
        final List<Issue> piFeatures = issueService.getProductIncrementFeatures(piDTO.getProgramIncrementName());
        final List<String> piFeaturesKeys = piFeatures
            .stream()
            .map(Issue::getNumber)
            .collect(Collectors.toList());
        final List<String> boardPIFeaturesKeys = issueService.getProgramIncrementFeaturesByBoard(
            dashboard.getBoards(),
            piFeaturesKeys
        );
        final List<IssueDTO> boardPIFeatures = piFeatures.stream()
            .filter(f -> boardPIFeaturesKeys.contains(f.getNumber()) || containsDashboardKeyword(dashboard, f))
            .map(IssueMapper::map)
            .collect(Collectors.toList());

        //Get stories belonging to this board and this PI
        final List<String> keys = boardPIFeatures.stream()
            .map(IssueDTO::getJiraKey)
            .collect(Collectors.toList());
        final List<Issue> piIssues = issueService.getFeatureRelatedIssues(keys);
        final List<IssueDTO> boardPIIssues = piIssues.stream()
            .filter((f) -> containsDashboardKeyword(dashboard, f))
            .map(IssueMapper::map)
            .collect(Collectors.toList());

        //Get epics belonging to this board and this PI
        final List<String> parentsKeys = piFeatures.stream()
            .filter(f -> f.getParentsKeys() != null)
            .map(Issue::getParentsKeys)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        final List<Issue> piEpics = issueService.getEpicsByNumber(parentsKeys);
        final List<IssueDTO> boardPIEpics = piEpics.stream()
            .filter((f) -> containsDashboardKeyword(dashboard, f))
            .map(IssueMapper::map)
            .collect(Collectors.toList());

        return new ProgramIncrementDTO()
            .setProgramIncrementEpics(boardPIEpics)
            .setProgramIncrementFeatures(boardPIFeatures)
            .setProgramIncrementStories(boardPIIssues);
    }

    private boolean containsDashboardKeyword(final DashboardDTO dashboard, final Issue f) {
        return f.getKeywords() != null && f.getKeywords().stream().anyMatch((k) -> dashboard.getBoards().contains(k));
    }

    private boolean findIfLocalDateIsInRange(final String date1, final String date2) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        final LocalDate dateTime1 = LocalDate.parse(date1, formatter);
        final LocalDate dateTime2 = LocalDate.parse(date2, formatter);

        final LocalDate now = LocalDate.now();

        return (now.isEqual(dateTime1) || now.isEqual(dateTime2))
            || (dateTime1.isBefore(now) && dateTime2.isAfter(now));
    }

}
