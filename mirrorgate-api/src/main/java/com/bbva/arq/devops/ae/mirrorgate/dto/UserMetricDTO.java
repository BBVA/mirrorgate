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

package com.bbva.arq.devops.ae.mirrorgate.dto;

public class UserMetricDTO {

    private String identifier;

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private long timestamp;

    private Double value;

    private Long sampleSize;

    private Double oneHourValue;

    private Long oneHourSampleSize;

    private Double eightHoursValue;

    private Long eightHoursSampleSize;

    private Double oneDayValue;

    private Long oneDaySampleSize;

    private Double sevenDaysValue;

    private Long sevenDaysSampleSize;

    private Double thirtyDaysValue;

    private Long thirtyDaysSampleSize;

    private Double ninetyDaysValue;

    private Long ninetyDaysSampleSize;

    /* Associated collector ID */
    private String collectorId;

    public String getIdentifier() {
        return identifier != null ? identifier : viewId
            + platform
            + appVersion
            + name
            + collectorId;
    }

    public UserMetricDTO setIdentifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getViewId() {
        return viewId;
    }

    public UserMetricDTO setViewId(final String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public UserMetricDTO setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public UserMetricDTO setPlatform(final String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserMetricDTO setName(final String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public UserMetricDTO setValue(final Double value) {
        this.value = value;
        return this;
    }

    public Long getSampleSize() {
        return sampleSize;
    }

    public UserMetricDTO setSampleSize(final long sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UserMetricDTO setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Double getOneHourValue() {
        return oneHourValue;
    }

    public UserMetricDTO setOneHourValue(final Double oneHourValue) {
        this.oneHourValue = oneHourValue;
        return this;
    }

    public Long getOneHourSampleSize() {
        return oneHourSampleSize;
    }

    public UserMetricDTO setOneHourSampleSize(final Long oneHourSampleSize) {
        this.oneHourSampleSize = oneHourSampleSize;
        return this;
    }

    public Double getEightHoursValue() {
        return eightHoursValue;
    }

    public UserMetricDTO setEightHoursValue(final Double eightHoursValue) {
        this.eightHoursValue = eightHoursValue;
        return this;
    }

    public Long getEightHoursSampleSize() {
        return eightHoursSampleSize;
    }

    public UserMetricDTO setEightHoursSampleSize(final Long eightHoursSampleSize) {
        this.eightHoursSampleSize = eightHoursSampleSize;
        return this;
    }

    public Double getOneDayValue() {
        return oneDayValue;
    }

    public UserMetricDTO setOneDayValue(final Double oneDayValue) {
        this.oneDayValue = oneDayValue;
        return this;
    }

    public Long getOneDaySampleSize() {
        return oneDaySampleSize;
    }

    public UserMetricDTO setOneDaySampleSize(final Long oneDaySampleSize) {
        this.oneDaySampleSize = oneDaySampleSize;
        return this;
    }

    public Double getSevenDaysValue() {
        return sevenDaysValue;
    }

    public UserMetricDTO setSevenDaysValue(final Double sevenDaysValue) {
        this.sevenDaysValue = sevenDaysValue;
        return this;
    }

    public Long getSevenDaysSampleSize() {
        return sevenDaysSampleSize;
    }

    public UserMetricDTO setSevenDaysSampleSize(final Long sevenDaysSampleSize) {
        this.sevenDaysSampleSize = sevenDaysSampleSize;
        return this;
    }

    public Double getThirtyDaysValue() {
        return thirtyDaysValue;
    }

    public UserMetricDTO setThirtyDaysValue(final Double thirtyDaysValue) {
        this.thirtyDaysValue = thirtyDaysValue;
        return this;
    }

    public Long getThirtyDaysSampleSize() {
        return thirtyDaysSampleSize;
    }

    public UserMetricDTO setThirtyDaysSampleSize(final Long thirtyDaysSampleSize) {
        this.thirtyDaysSampleSize = thirtyDaysSampleSize;
        return this;
    }

    public Double getNinetyDaysValue() {
        return ninetyDaysValue;
    }

    public UserMetricDTO setNinetyDaysValue(final Double ninetyDaysValue) {
        this.ninetyDaysValue = ninetyDaysValue;
        return this;
    }

    public Long getNinetyDaysSampleSize() {
        return ninetyDaysSampleSize;
    }

    public UserMetricDTO setNinetyDaysSampleSize(final Long ninetyDaysSampleSize) {
        this.ninetyDaysSampleSize = ninetyDaysSampleSize;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public UserMetricDTO setCollectorId(final String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

}
