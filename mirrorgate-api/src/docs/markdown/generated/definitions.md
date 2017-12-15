## Definitions
### ApplicationReviewsDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|appId||false|string||
|appName||false|string||
|commentId||false|string||
|platform||false|enum (Android, IOS, Windows, WindowsPhone, Unknown, All)||


### BuildDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|branch||false|string||
|buildStatus||false|string||
|buildUrl||false|string||
|culprits||false|string array||
|duration||false|integer (int64)||
|endTime||false|integer (int64)||
|keywords||false|string array||
|number||false|string||
|projectName||false|string||
|repoName||false|string||
|startTime||false|integer (int64)||
|timestamp||false|integer (int64)||


### BuildStats
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|count||false|integer (int64)||
|duration||false|number (double)||
|failureRate||false|number (double)||
|failureTendency||false|enum (up, down, equal)||


### DashboardDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|adminUsers||false|string array||
|aggregatedDashboards||false|string array||
|analyticViews||false|string array||
|applications||false|string array||
|author||false|string||
|boards||false|string array||
|category||false|string||
|codeRepos||false|string array||
|displayName||false|string||
|errorsRateAlertingLevelError||false|number (float)||
|errorsRateAlertingLevelWarning||false|number (float)||
|filters||false|Filters||
|lastModification||false|integer (int64)||
|lastUserEdit||false|string||
|lastVersion||false|string||
|logoUrl||false|string||
|marketsStatsDays||false|integer (int32)||
|name||false|string||
|programIncrement||false|string||
|responseTimeAlertingLevelError||false|number (float)||
|responseTimeAlertingLevelWarning||false|number (float)||
|sProductName||false|string||
|skin||false|string||
|slackChannel||false|string||
|slackTeam||false|string||
|slackToken||false|string||
|status||false|enum (DELETED, ACTIVE, TRANSIENT)||
|teamMembers||false|string array||
|type||false|enum (Aggregate, Detail)||
|urlAlerts||false|string||
|urlAlertsAuthorization||false|string||


### FeatureStats
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|backlogEstimate||false|number (double)||
|sprintStats||false|SprintStats||


### Filters
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|branch||false|object||
|status||false|object||
|timeSpan||false|integer (int32)||


### HistoricUserMetricDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|appVersion||false|string||
|collectorId||false|string||
|name||false|string||
|platform||false|string||
|sampleSize||false|number (double)||
|timestamp||false|integer (int64)||
|value||false|number (double)||
|viewId||false|string||


### IssueDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|collectorId||false|string||
|estimate||false|number (double)||
|id||false|integer (int64)||
|jiraKey||false|string||
|keywords||false|string array||
|name||false|string||
|parentId||false|string array||
|parentKey||false|string array||
|piNames||false|string array||
|priority||false|enum (HIGHEST, HIGH, MEDIUM, LOW, LOWEST)||
|project||false|ProjectDTO||
|sprint||false|SprintDTO||
|status||false|enum (IN_PROGRESS, DONE, BACKLOG, WAITING, IMPEDED)||
|type||false|string||
|updatedDate||false|string (date-time)||
|url||false|string||


### ProgramIncrementDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|programIncrementEndDate||false|string (date-time)||
|programIncrementEpics||false|IssueDTO array||
|programIncrementFeatures||false|IssueDTO array||
|programIncrementName||false|string||
|programIncrementStartDate||false|string (date-time)||
|programIncrementStories||false|IssueDTO array||


### ProjectDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|id||false|integer (int64)||
|key||false|string||
|name||false|string||


### ResponseEntity
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|body||false|object||
|statusCode||false|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)||
|statusCodeValue||false|integer (int32)||


### Review
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|amount||false|integer (int32)||
|appname||false|string||
|authorName||false|string||
|comment||false|string||
|commentId||false|string||
|commentTitle||false|string||
|platform||false|enum (Android, IOS, Windows, WindowsPhone, Unknown, All)||
|starrating||false|number (double)||
|timestamp||false|integer (int64)||
|url||false|string||


### SprintDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|completeDate||false|string (date-time)||
|endDate||false|string (date-time)||
|id||false|string||
|issues||false|IssueDTO array||
|name||false|string||
|startDate||false|string (date-time)||
|status||false|enum (ACTIVE, CLOSED, FUTURE)||


### SprintStats
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|daysDurationAvg||false|number (double)||
|estimateAvg||false|number (double)||


### SseEmitter
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|timeout||false|integer (int64)||


### UserMetricDTO
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|appVersion||false|string||
|collectorId||false|string||
|name||false|string||
|platform||false|string||
|sampleSize||false|number (double)||
|timestamp||false|integer (int64)||
|value||false|number (double)||
|viewId||false|string||


