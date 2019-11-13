/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.repository;


import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepositoryImpl.ProgramIncrementNamesAggregationResult;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueType;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class IssueRepositoryImplTest {

    @Autowired
    private IssueRepository issueRepository;

    @Before
    public void init(){
        issueRepository.save(createFeature(Arrays.asList("PI2", "PI5", "PI3"), "feature1"));
        issueRepository.save(createFeature(Arrays.asList("PI3", "PI4", "PI1"), "feature2"));
        issueRepository.save(createActiveStory("mirrorgate", "issue1"));
        issueRepository.save(createActiveStory("not_mirrorgate", "issue1"));
        issueRepository.save(createActiveStory("mirrorgate", "issue2"));
        issueRepository.save(createActiveStory("not_mirrorgate", "issue2"));
    }

    @After
    public void clean(){
        issueRepository.deleteAll();
    }

    @Test
    public void testFeatureAndPIComeFromTeam(){
        final List<String> boardPIFeatures = issueRepository.programIncrementBoardFeatures(Collections.singletonList("mirrorgate"), Arrays.asList("issue1", "issue2"));

        assertEquals(2, boardPIFeatures.size());
    }


    @Test
    public void testAggregationWithResults(){
        final ProgramIncrementNamesAggregationResult piNames = issueRepository.getProductIncrementFromPiPattern(Pattern.compile("^PI.*$"));

        assertEquals(piNames.getPiNames().size(), 5);
        assertTrue(piNames.getPiNames().contains("PI1"));
        assertTrue(piNames.getPiNames().contains("PI2"));
        assertTrue(piNames.getPiNames().contains("PI3"));
        assertTrue(piNames.getPiNames().contains("PI4"));
        assertTrue(piNames.getPiNames().contains("PI5"));
    }

    @Test
    public void testAggregationWithoutResults(){
        final ProgramIncrementNamesAggregationResult piNames = issueRepository.getProductIncrementFromPiPattern(Pattern.compile("aaa"));
        assertNull(piNames);
    }

    private Issue createFeature(List<String> piNames, String number) {
        return new Issue()
            .setProjectName("mirrorgate")
            .setPiNames(piNames)
            .setType(IssueType.FEATURE.getName())
            .setKeywords(Collections.singletonList("mirrorgate"))
            .setNumber(number);
    }

    private static Issue createActiveStory(String projectName, String parentKey) {
        return new Issue()
            .setIssueId(ObjectId.get().toString())
            .setSprintAssetState("Active")
            .setProjectName(projectName)
            .setNumber("story_name")
            .setParentsKeys(Collections.singletonList(parentKey))
            .setKeywords(Collections.singletonList(projectName));
    }

}
