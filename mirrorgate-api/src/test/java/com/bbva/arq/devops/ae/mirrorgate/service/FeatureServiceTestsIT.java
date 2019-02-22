package com.bbva.arq.devops.ae.mirrorgate.service;

import static junit.framework.TestCase.assertEquals;

import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeatureServiceTestsIT {


    @Autowired
    private FeatureServiceImpl featureService;

    @Autowired
    private FeatureRepository repository;

    @Before
    public void init(){
        Feature feature1 = TestObjectFactory.createActiveStory()
            .setsId("1234")
            .setCollectorId("collectorId");

        Feature feature2 = TestObjectFactory.createActiveStory()
            .setsId("1234")
            .setCollectorId("collectorId");

        Feature feature3 = TestObjectFactory.createActiveStory()
            .setsId("12345")
            .setCollectorId("collectorId");

        Feature feature4 = TestObjectFactory.createActiveStory()
            .setsId("12345")
            .setCollectorId("collectorId");

        repository.saveAll(Arrays.asList(feature1, feature2, feature3, feature4));
    }


    @Test
    public void testUpdateDuplicatedFeatures(){

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "collectorId", "name");

        featureService.saveOrUpdateStories(Collections.singletonList(issueDTO1), "collectorId");

        assertEquals(2, repository.count());
    }

    @Test
    public void testUpdateSeveralDuplicatedFeatures(){

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "collectorId", "name");
        IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345L, "collectorId", "name");

        featureService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorId");

        assertEquals(2, repository.count());
    }

    @Test
    public void testUpdateWithNoFeatures(){
        repository.deleteAll();

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234L, "collectorId", "name");
        IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345L, "collectorId", "name");

        featureService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorId");

        assertEquals(2, repository.count());
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

}
