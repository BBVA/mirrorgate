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

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper.map;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.support.IssuePriority;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IssueMapperTests {

    @Test
    public void itShouldMapAllFields() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Feature feature = new Feature()
                .setsStatus(IssueStatus.DONE.getName())
                .setsSprintAssetState(SprintStatus.CLOSED.name())
                .setPriority(IssuePriority.MEDIUM.getName())
                ;

        MapperTestingSupport.initializeTypicalSetters(feature);
        MapperTestingSupport.assertBeanValues(feature, map(map(feature)));
    }

}
