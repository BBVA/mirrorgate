/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate;

import com.bbva.arq.devops.ae.mirrorgate.cron.handler.EventHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import java.util.EnumSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MirrorGateApiApplicationTests {

    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void contextLoads() {
        final long port = (Math.round(Math.random() * 10000) + 10000);
        MirrorGateApiApplication.main(new String[]{"--server.port=" + port});
    }

    @Test
    public void testBeanPerEventType() {
        EnumSet.allOf(EventType.class)
            .stream().filter((e) -> e != EventType.PING)
            .forEach(eventType -> beanFactory.getBean(eventType.getValue(), EventHandler.class));

    }
}
