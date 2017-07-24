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
package com.bbva.arq.devops.ae.mirrorgate.util;

import static com.bbva.arq.devops.ae.mirrorgate.core.dto.FailureTendency.down;
import static com.bbva.arq.devops.ae.mirrorgate.core.dto.FailureTendency.equal;
import static com.bbva.arq.devops.ae.mirrorgate.core.dto.FailureTendency.up;
import static com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatsUtils.failureTendency;
import static org.junit.Assert.assertEquals;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.FailureTendency;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BuildStatsUtilsTests {

    @Test
    public void itShouldReturnAnEmptyResultWhenCalledWithNoResults() {
        BuildStats stats = BuildStatsUtils.combineBuildStats();

        assertEquals(stats.getCount(), 0);
        assertEquals(stats.getDuration(), .0,0);
        assertEquals(stats.getFailureRate(), 0);

    }


    @Test
    public void itShouldReturnAnEmptyResultWhenCalledWithEmptyResults() {
        BuildStats stats = BuildStatsUtils.combineBuildStats(new BuildStats());

        assertEquals(stats.getCount(), 0);
        assertEquals(stats.getDuration(), .0,0);
        assertEquals(stats.getFailureRate(), 0);

    }

    @Test
    public void itShouldAddCounts() {
        BuildStats stats = BuildStatsUtils.combineBuildStats(
                new BuildStats().setCount(1),
                new BuildStats().setCount(2),
                new BuildStats().setCount(3)
        );

        assertEquals(stats.getCount(), 6);

    }

    @Test
    public void itShouldAverageFailureRates() {
        BuildStats stats = BuildStatsUtils.combineBuildStats(
                new BuildStats().setCount(1),
                new BuildStats().setCount(2),
                new BuildStats().setCount(3).setFailureRate(100)
        );

        assertEquals(stats.getFailureRate(), 50);

    }

    @Test
    public void itShouldAverageDurations() {
        BuildStats stats = BuildStatsUtils.combineBuildStats(
                new BuildStats().setCount(1).setDuration(100),
                new BuildStats().setCount(2).setDuration(200),
                new BuildStats().setCount(4).setDuration(400)
        );

        assertEquals(stats.getDuration(), 300,0);

    }

    @Test
    public void itShouldReturnTheCorrectFailureTendency() {
        BuildStats statsSevenDaysBefore = new BuildStats().setFailureRate(100);
        BuildStats statsFifteenDaysBefore = new BuildStats().setFailureRate(300);

        assertEquals(failureTendency(statsSevenDaysBefore.getFailureRate(),statsFifteenDaysBefore.getFailureRate()), up);
        assertEquals(failureTendency(statsFifteenDaysBefore.getFailureRate(),statsSevenDaysBefore.getFailureRate()), down);
        assertEquals(failureTendency(statsSevenDaysBefore.getFailureRate(),statsSevenDaysBefore.getFailureRate()), equal);
    }
}
