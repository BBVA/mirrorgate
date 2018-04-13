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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardType;
import com.bbva.arq.devops.ae.mirrorgate.support.Filters;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DashboardMapperTests {

    @Test
    public void itShouldMapAllFields() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Dashboard dashboard = new Dashboard()
                .setStatus(DashboardStatus.ACTIVE)
                .setType(DashboardType.Detail.name())
                .setFilters(new Filters())
                .setInfraCost(false)
                .setLastTimeUsed(1L);

        MapperTestingSupport.initializeTypicalSetters(dashboard);
        MapperTestingSupport.assertBeanValues(dashboard, map(map(dashboard)));
    }

}
