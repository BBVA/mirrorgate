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

package com.bbva.arq.devops.ae.mirrorgate.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class LocalDateTimeHelper {

    private LocalDateTimeHelper(){}


    public static long getTimestampPeriod(final long timestamp, final ChronoUnit chronoUnit) {

        final Instant instant = Instant.ofEpochMilli(timestamp);

        final LocalDateTime metricTimestamp = LocalDateTime.ofInstant(instant,
            TimeZone.getTimeZone("UTC").toZoneId()).truncatedTo(chronoUnit);

        return metricTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long getTimestampForNUnitsAgo(final int numberOfUnits, final ChronoUnit chronoUnit) {

        final LocalDateTime daysAgo = LocalDateTime.now(
            ZoneId.of("UTC")).minus(numberOfUnits, chronoUnit).truncatedTo(chronoUnit);

        return daysAgo.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long getTimestampForOneMonthAgo() {
        return LocalDateTime.now(ZoneId.of("UTC")).minusDays(30).toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
