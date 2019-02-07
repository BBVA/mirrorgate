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

package com.bbva.arq.devops.ae.mirrorgate.dto;

public class BuildStats {

    private Double duration;
    private Long count;
    private Double failureRate;
    private FailureTendency failureTendency;

    public double getDuration() {
        return duration;
    }

    public BuildStats setDuration(Double duration) {
        this.duration = duration;
        return this;
    }

    public long getCount() {
        return count;
    }

    public BuildStats setCount(long count) {
        this.count = count;
        return this;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public BuildStats setFailureRate(double failureRate) {
        this.failureRate = failureRate;
        return this;
    }

    public FailureTendency getFailureTendency() {
        return failureTendency;
    }

    public BuildStats setFailureTendency(FailureTendency failureTendency) {
        this.failureTendency = failureTendency;
        return this;
    }
}
