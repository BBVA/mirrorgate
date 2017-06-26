package com.bbva.arq.devops.ae.mirrorgate.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.util.Arrays;
import java.util.List;
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
        featureRepository.save(createFeature(Arrays.asList("PI1","PI2","PI3")));
        featureRepository.save(createFeature(Arrays.asList("PI3","PI4","PI5")));
    }


    @Test
    public void testAggregationWithResults(){
        ProgramIncrementNamesAggregationResult piNames = featureRepository.getProductIncrementFromFeatures(Arrays.asList("mirrorgate"));

        assertEquals(piNames.getPiNames().size(), 5);
        assertTrue(piNames.getPiNames().contains("PI1"));
        assertTrue(piNames.getPiNames().contains("PI2"));
        assertTrue(piNames.getPiNames().contains("PI3"));
        assertTrue(piNames.getPiNames().contains("PI4"));
        assertTrue(piNames.getPiNames().contains("PI5"));
    }

    @Test
    public void testAggregationWithoutResults(){
        ProgramIncrementNamesAggregationResult piNames = featureRepository.getProductIncrementFromFeatures(Arrays.asList("mirrorgate2"));
        assertEquals(piNames, null);
    }

    private Feature createFeature(List<String> piNames){
        Feature feature = new Feature();

        feature.setsProjectName("mirrorgate");
        feature.setsPiNames(piNames);
        feature.setsTypeName("Feature");
        feature.setKeywords(Arrays.asList("mirrorgate"));

        return feature;
    }

}
