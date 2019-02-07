package com.bbva.arq.devops.ae.mirrorgate.dto;

import java.util.Date;
import java.util.List;

public class ProgramIncrementDTO {

    private String programIncrementName;
    private List<IssueDTO> programIncrementEpics;
    private List<IssueDTO> programIncrementFeatures;
    private List<IssueDTO> programIncrementStories;
    private Date startDate;
    private Date endDate;

    public String getProgramIncrementName() {
        return programIncrementName;
    }

    public ProgramIncrementDTO setProgramIncrementName(String programIncrementName) {
        this.programIncrementName = programIncrementName;
        return this;
    }

    public List<IssueDTO> getProgramIncrementFeatures() {
        return programIncrementFeatures;
    }

    public ProgramIncrementDTO setProgramIncrementFeatures(List<IssueDTO> programIncrementFeatures) {
        this.programIncrementFeatures = programIncrementFeatures;
        return this;
    }

    public List<IssueDTO> getProgramIncrementStories() {
        return programIncrementStories;
    }

    public ProgramIncrementDTO setProgramIncrementStories(List<IssueDTO> programIncrementStories) {
        this.programIncrementStories = programIncrementStories;
        return this;
    }

    public List<IssueDTO> getProgramIncrementEpics() {
        return programIncrementEpics;
    }

    public ProgramIncrementDTO setProgramIncrementEpics(List<IssueDTO> programIncrementEpics) {
        this.programIncrementEpics = programIncrementEpics;
        return this;
    }

    public Date getProgramIncrementStartDate() {
        return startDate == null ? null : new Date(startDate.getTime());
    }

    public ProgramIncrementDTO setProgramIncrementStartDate(Date startDate) {
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        return this;
    }

    public Date getProgramIncrementEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

    public ProgramIncrementDTO setProgramIncrementEndDate(Date endDate) {
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        return this;
    }

}
