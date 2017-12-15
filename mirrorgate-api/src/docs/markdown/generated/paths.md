## Paths
### getApplicationsInfo
```
GET /api/applications
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|ApplicationReviewsDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* application-controller

### createBuilds
```
POST /api/builds
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|request|request|true|BuildDTO||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|BuildDTO|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* build-controller

### getLastExecutionDate
```
GET /api/collectors/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string (date-time)|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* collector-controller

### setLastExecutionDate
```
PUT /api/collectors/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|string||
|BodyParameter|executionDate|executionDate|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|ResponseEntity|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* collector-controller

### saveOrUpdateIssues
```
POST /api/issues
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|issues|issues|true|IssueDTO array||
|QueryParameter|collectorId|collectorId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Iterable«IssueDTO»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* feature-controller

### deleteStory
```
DELETE /api/issues/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||
|QueryParameter|collectorId|collectorId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* feature-controller

### createReviews
```
POST /api/reviews
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|reviews|reviews|true|Iterable«Review»||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* review-controller

### getChangingSprintsSample
```
GET /api/sprints/changing-sample
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|QueryParameter|collectorId|collectorId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|SprintDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* sprint-controller

### getChangingSprint
```
GET /api/sprints/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||
|QueryParameter|collectorId|collectorId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|SprintDTO|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* sprint-controller

### getAnalyticViewIdsByCollectorId
```
GET /api/user-metrics
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|QueryParameter|collectorId|collectorId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|UserMetricDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* user-metrics-controller

### saveMetrics
```
POST /api/user-metrics
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|metrics|metrics|true|Iterable«UserMetricDTO»||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|UserMetricDTO array|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* user-metrics-controller

### getAnalyticViewIds
```
GET /api/user-metrics/analytic-views
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* user-metrics-controller

### getDashboardChannels
```
GET /backoffice/utils/slack/channels
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|QueryParameter|dashboard|dashboard|false|string||
|QueryParameter|token|token|false|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* back-office-slack-utils-controller

### getSlackCode
```
GET /backoffice/utils/slack/code-capturer
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|QueryParameter|code|code|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* text/html

#### Tags

* back-office-slack-utils-controller

### getSlackToken
```
GET /backoffice/utils/slack/token-generator
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|QueryParameter|code|code|true|string||
|QueryParameter|clientId|clientId|true|string||
|QueryParameter|team|team|true|string||
|QueryParameter|clientSecret|clientSecret|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* text/plain

#### Tags

* back-office-slack-utils-controller

### getActiveDashboards
```
GET /dashboards
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|DashboardDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* dashboard-controller

### newDashboard
```
POST /dashboards
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|request|request|true|DashboardDTO||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|DashboardDTO|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* dashboard-controller

### updateDashboard
```
PUT /dashboards/{name}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||
|BodyParameter|request|request|true|DashboardDTO||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|DashboardDTO|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* dashboard-controller

### deleteDashboard
```
DELETE /dashboards/{name}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* dashboard-controller

### getApplicationReviewRatings
```
GET /dashboards/{name}/applications
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* review-controller

### getBugs
```
GET /dashboards/{name}/bugs
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* bug-controller

### getBuildsByBoardName
```
GET /dashboards/{name}/builds
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* build-controller

### getStats
```
GET /dashboards/{name}/builds/rate
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|BuildStats|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* build-controller

### getDashboard
```
GET /dashboards/{name}/details
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|DashboardDTO|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* dashboard-controller

### getHistoricUserMetric
```
GET /dashboards/{name}/historic-user-metrics
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|HistoricUserMetricDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* */*

#### Tags

* historic-user-metrics-controller

### getFile
```
GET /dashboards/{name}/image
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* */*

#### Tags

* dashboard-controller

### uploadFile
```
POST /dashboards/{name}/image
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||
|FormDataParameter|uploadfile|uploadfile|true|file||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* multipart/form-data

#### Produces

* */*

#### Tags

* dashboard-controller

### getWebSocket
```
GET /dashboards/{name}/notifications
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* notification-controller

### getAtiveUserStories
```
GET /dashboards/{name}/programincrement
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|ProgramIncrementDTO|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* program-increment-controller

### getAtiveUserStories
```
GET /dashboards/{name}/stories
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* feature-controller

### getStoriesStats
```
GET /dashboards/{name}/stories/_stats
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|FeatureStats|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* feature-controller

### getMetricsForBoard
```
GET /dashboards/{name}/user-metrics
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|name|name|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|UserMetricDTO array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/json

#### Tags

* user-metrics-controller

### serverSideEmitter
```
GET /emitter/{dashboardId}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|dashboardId|dashboardId|true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|SseEmitter|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* */*

#### Tags

* server-sent-events-controller

### createReviewsOfApplication
```
POST /reviews/{appid}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|appid|appid|true|string||
|QueryParameter|url|url|false|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|object|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* */*

#### Tags

* review-controller

