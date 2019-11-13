package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IssueServiceTestsIT {


    @Autowired
    private IssueServiceImpl issueService;

    @Autowired
    private IssueRepository repository;

    @Before
    public void init(){
        final Issue issue1 = TestObjectFactory.createActiveStory()
            .setIssueId("1234")
            .setCollectorId("collectorId");

        final Issue issue2 = TestObjectFactory.createActiveStory()
            .setIssueId("1234")
            .setCollectorId("collectorId");

        final Issue issue3 = TestObjectFactory.createActiveStory()
            .setIssueId("12345")
            .setCollectorId("collectorId");

        final Issue issue4 = TestObjectFactory.createActiveStory()
            .setIssueId("12345")
            .setCollectorId("collectorId");

        repository.saveAll(Arrays.asList(issue1, issue2, issue3, issue4));
    }


    @Test
    public void testUpdateDuplicatedIssues() {

        final IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "name", "collectorId");

        issueService.saveOrUpdateStories(Collections.singletonList(issueDTO1), "collectorId");

        assertEquals(2, repository.count());
    }

    @Test
    public void testUpdateSeveralDuplicatedIssues() {

        final IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "name", "collectorId");
        final IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345L, "name", "collectorId");

        issueService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorId");

        assertEquals(2, repository.count());
    }

    @Test
    public void testUpdateWithNoIssues() {
        repository.deleteAll();

        final IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "name", "collectorId");
        final IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345L, "name", "collectorId");

        issueService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorId");

        assertEquals(2, repository.count());
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

}
