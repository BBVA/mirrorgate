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

package com.bbva.arq.devops.ae.mirrorgate.core.model;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BuildStatusTest {

    @Test
    public void CheckBuildStatusFromString() {
        Assert.assertEquals(BuildStatus.fromString("asdlfkasj"), BuildStatus.Unknown);
        Assert.assertEquals(BuildStatus.fromString("ABORTED"), BuildStatus.Aborted);
        Assert.assertEquals(BuildStatus.fromString("INPROGRESS"), BuildStatus.InProgress);
        Assert.assertEquals(BuildStatus.fromString("FAILURE"), BuildStatus.Failure);
        Assert.assertEquals(BuildStatus.fromString("NOT_BUILT"), BuildStatus.NotBuilt);
        Assert.assertEquals(BuildStatus.fromString("SuCcEss"), BuildStatus.Success);
        Assert.assertEquals(BuildStatus.fromString("UNSTABLE"), BuildStatus.Unstable);
        Assert.assertEquals(BuildStatus.fromString("DELETED"), BuildStatus.Deleted);
    }

}
