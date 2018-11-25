package com.bbva.arq.devops.ae.mirrorgate.service;

import static junit.framework.TestCase.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.Arrays;
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

        Feature feature1 = TestObjectFactory.createActiveStory();
        feature1.setsId("1234");
        feature1.setCollectorId("collectorid");


        Feature feature2 = TestObjectFactory.createActiveStory();
        feature2.setsId("1234");
        feature2.setCollectorId("collectorid");

        Feature feature3 = TestObjectFactory.createActiveStory();
        feature3.setsId("12345");
        feature3.setCollectorId("collectorid");

        Feature feature4 = TestObjectFactory.createActiveStory();
        feature4.setsId("12345");
        feature4.setCollectorId("collectorid");

        repository.saveAll(Arrays.asList(feature1, feature2, feature3, feature4));

    }


    @Test
    public void testUpdateDuplicatedFeatures(){

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234l, "collectorid", "name");

        featureService.saveOrUpdateStories(Arrays.asList(issueDTO1), "collectorid");

        assertTrue(repository.count() == 3);
    }

    @Test
    public void testUpdateSeveralDuplicatedFeatures(){

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234l, "collectorid", "name");
        IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345l, "collectorid", "name");

        featureService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorid");

        assertTrue(repository.count() == 2);
    }

    @Test
    public void testUpdateWithNoFeatures(){
        repository.deleteAll();

        IssueDTO issueDTO1 = TestObjectFactory.createIssueDTO(1234l, "collectorid", "name");
        IssueDTO issueDTO2 = TestObjectFactory.createIssueDTO(12345l, "collectorid", "name");

        featureService.saveOrUpdateStories(Arrays.asList(issueDTO1, issueDTO2), "collectorid");

        assertTrue(repository.count() == 2);
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

}
