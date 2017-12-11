package com.bbva.arq.devops.ae.mirrorgate.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class LocalDateTimeHelper {

    private LocalDateTimeHelper(){}


    public static long getTimestampPeriod(long timestamp){

        Instant instant = Instant.ofEpochMilli(timestamp);

        LocalDateTime metricTimestamp = LocalDateTime.ofInstant(instant,
            TimeZone.getTimeZone("UTC").toZoneId()).truncatedTo(ChronoUnit.HOURS);

        return metricTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long getTimestampForNDaysAgo(int days){

        LocalDateTime daysAgo =
            LocalDateTime.now(ZoneId.of("UTC")).minusDays(days).truncatedTo(ChronoUnit.HOURS);

        return daysAgo.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
