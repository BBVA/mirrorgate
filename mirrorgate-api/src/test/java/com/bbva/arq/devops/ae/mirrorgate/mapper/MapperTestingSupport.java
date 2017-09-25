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

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MapperTestingSupport {

    public static void assertBeanValues(Object o1, Object o2) throws IllegalAccessException, InvocationTargetException {
        for (Method method : o1.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                Object value = method.invoke(o1);

                Assert.assertNotNull(method.getName(), value);
                Assert.assertEquals(method.getName(), value, method.invoke(o2));
            }
        }
    }

    public static void initializeTypicalSetters(Object o) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int count = 0;

        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("set")) {
                String getter = method.getName().replaceFirst("set", "get");
                if(o.getClass().getDeclaredMethod(getter).invoke(o) == null) {
                    Class argumentType = method.getParameterTypes()[0];
                    if (argumentType == String.class) {
                        method.invoke(o, "" + count++);
                    } else if (argumentType == Integer.class) {
                        method.invoke(o, count++);
                    } else if (argumentType == Long.class) {
                        method.invoke(o, Long.valueOf(count++));
                    } else if (argumentType == Date.class) {
                        method.invoke(o, new Date());
                    } else if (argumentType == Double.class) {
                        method.invoke(o, Double.valueOf(count++));
                    } else if (argumentType == Float.class) {
                        method.invoke(o, Float.valueOf(count++));
                    } else if (argumentType == List.class) {
                        method.invoke(o, Arrays.asList());
                    }
                }
            }
        }
    }
}