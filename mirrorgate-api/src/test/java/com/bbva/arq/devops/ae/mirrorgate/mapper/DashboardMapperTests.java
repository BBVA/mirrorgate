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

import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardType;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Filters;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by alfonso on 25/09/17.
 */

@RunWith(JUnit4.class)
public class DashboardMapperTests {

    @Test
    public void itShouldMapAllFields() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Dashboard dashboard = new Dashboard()
                .setStatus(DashboardStatus.ACTIVE)
                .setType(DashboardType.Detail.name())
                .setFilters(new Filters());

        MapperTestingSupport.initializeTypicalSetters(dashboard);
        MapperTestingSupport.assertBeanValues(dashboard, map(map(dashboard)));
    }

}
