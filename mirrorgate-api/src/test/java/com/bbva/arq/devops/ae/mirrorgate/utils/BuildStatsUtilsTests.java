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

package com.bbva.arq.devops.ae.mirrorgate.utils;

import static com.bbva.arq.devops.ae.mirrorgate.dto.FailureTendency.down;
import static com.bbva.arq.devops.ae.mirrorgate.dto.FailureTendency.equal;
import static com.bbva.arq.devops.ae.mirrorgate.dto.FailureTendency.up;
import static com.bbva.arq.devops.ae.mirrorgate.utils.BuildStatsUtils.failureTendency;
import static org.junit.Assert.assertEquals;

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BuildStatsUtilsTests {

    @Test
    public void itShouldReturnAnEmptyResultWhenCalledWithNoResults() {
        final BuildStats stats = BuildStatsUtils.combineBuildStats();

        assertEquals(stats.getCount(), 0);
        assertEquals(stats.getDuration(), .0, 0);
        assertEquals(stats.getFailureRate(), .0, 0);
    }


    @Test
    public void itShouldReturnAnEmptyResultWhenCalledWithEmptyResults() {
        final BuildStats stats = BuildStatsUtils.combineBuildStats(new BuildStats());

        assertEquals(stats.getCount(), 0);
        assertEquals(stats.getDuration(), .0, 0);
        assertEquals(stats.getFailureRate(), .0, 0);
    }

    @Test
    public void itShouldAddCounts() {
        final BuildStats stats = BuildStatsUtils.combineBuildStats(
            new BuildStats().setCount(1),
            new BuildStats().setCount(2),
            new BuildStats().setCount(3)
        );

        assertEquals(stats.getCount(), 6);
    }

    @Test
    public void itShouldAverageFailureRates() {
        final BuildStats stats = BuildStatsUtils.combineBuildStats(
            new BuildStats().setCount(1),
            new BuildStats().setCount(2),
            new BuildStats().setCount(3).setFailureRate(100)
        );

        assertEquals(stats.getFailureRate(), 50.0, 0);
    }

    @Test
    public void itShouldAverageDurations() {
        final BuildStats stats = BuildStatsUtils.combineBuildStats(
            new BuildStats().setCount(1).setDuration(100),
            new BuildStats().setCount(2).setDuration(200),
            new BuildStats().setCount(4).setDuration(400)
        );

        assertEquals(stats.getDuration(), 300, 0);
    }

    @Test
    public void itShouldReturnTheCorrectFailureTendency() {
        assertEquals(failureTendency(100, 300), down);
        assertEquals(failureTendency(300, 100), up);
        assertEquals(failureTendency(10, 10), equal);
        assertEquals(failureTendency(0, 10), down);
    }
}