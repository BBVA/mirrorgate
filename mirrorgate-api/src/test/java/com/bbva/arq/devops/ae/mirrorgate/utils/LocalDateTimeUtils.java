package com.bbva.arq.devops.ae.mirrorgate.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

public class LocalDateTimeUtils {
    public static final long TODAY = LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC).toEpochMilli();
    public static final long YESTERDAY = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    public static final long ONE_WEEK_AGO = LocalDateTime.now(ZoneId.of("UTC")).minusDays(7).toInstant(ZoneOffset.UTC).toEpochMilli();
    public static final long ONE_MONTH_AGO = LocalDateTime.now(ZoneId.of("UTC")).minusDays(30).toInstant(ZoneOffset.UTC).toEpochMilli();
    public static final long THREE_HOURS_AGO = LocalDateTime.now(ZoneId.of("UTC")).minusHours(2).toInstant(ZoneOffset.UTC).toEpochMilli();
}
