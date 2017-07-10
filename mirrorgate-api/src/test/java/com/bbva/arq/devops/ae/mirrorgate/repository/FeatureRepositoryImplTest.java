package com.bbva.arq.devops.ae.mirrorgate.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class FeatureRepositoryImplTest {

    @Autowired
    private FeatureRepository featureRepository;

    @Before
    public void init(){
        featureRepository.save(createFeature(Arrays.asList("PI1","PI2","PI3"), "feature1"));
        featureRepository.save(createFeature(Arrays.asList("PI3","PI4","PI5"), "feature2"));
        featureRepository.save(createActiveStory("mirrorgate", "feature1"));
        featureRepository.save(createActiveStory("not_mirrorgate", "feature1"));
        featureRepository.save(createActiveStory("mirrorgate", "feature2"));
        featureRepository.save(createActiveStory("not_mirrorgate", "feature2"));
    }

    @After
    public void clean(){
        featureRepository.deleteAll();
    }

    @Test
    public void testFeatureAndPIComeFromTeam(){
        List<String> boardPIFeatures = featureRepository.programIncrementBoardFeatures(Arrays.asList("mirrorgate"), Arrays.asList("feature1", "feature2"));

        assertEquals(boardPIFeatures.size(), 2);
    }


    @Test
    public void testAggregationWithResults(){
        ProgramIncrementNamesAggregationResult piNames = featureRepository.getProductIncrementFromPiPattern(Pattern.compile("^PI.*$"));

        assertEquals(piNames.getPiNames().size(), 5);
        assertTrue(piNames.getPiNames().contains("PI1"));
        assertTrue(piNames.getPiNames().contains("PI2"));
        assertTrue(piNames.getPiNames().contains("PI3"));
        assertTrue(piNames.getPiNames().contains("PI4"));
        assertTrue(piNames.getPiNames().contains("PI5"));
    }

    @Test
    public void testAggregationWithoutResults(){
        ProgramIncrementNamesAggregationResult piNames = featureRepository.getProductIncrementFromPiPattern(Pattern.compile("aaa"));
        assertEquals(piNames, null);
    }

    private Feature createFeature(List<String> piNames, String sNumber){
        Feature feature = new Feature();

        feature.setsProjectName("mirrorgate");
        feature.setsPiNames(piNames);
        feature.setsTypeName("Feature");
        feature.setKeywords(Arrays.asList("mirrorgate"));
        feature.setsNumber(sNumber);

        return feature;
    }

    private static Feature createActiveStory(String sProjectName, String sParentKey) {
        Feature story = new Feature();

        story.setId(ObjectId.get());
        story.setsId(ObjectId.get().toString());
        story.setsSprintAssetState("Active");
        story.setsProjectName(sProjectName);
        story.setsNumber("story_name");
        story.setsParentKey(sParentKey);
        story.setKeywords(Arrays.asList(sProjectName));

        return story;
    }


}
