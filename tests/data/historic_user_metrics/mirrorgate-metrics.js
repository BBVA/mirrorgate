
var now = new Date();
var startOfMinute = parseInt((new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes())).getTime());
var startOfHour = parseInt((new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours())).getTime());
var startOfDay = parseInt((new Date(now.getFullYear(), now.getMonth(), now.getDate())).getTime());

db.getCollection('historic_user_metrics').insertMany([
  {
    "viewId" : "1234123",
    "identifier": "AWS/1234123/alb",
    "appVersion" : "2",
    "platform" : "Android",
    "name" : "availabilityRate",
    "value" : 99.99,
    "sampleSize": 1,
    "timestamp" : startOfMinute,
    "collectorId" : "collector1",
    "historicType" : "MINUTES"
  },
  {
    "viewId" : "1234123",
    "identifier": "AWS/1234123/alb",
    "appVersion" : "2",
    "platform" : "Android",
    "name" : "availabilityRate",
    "value" : 99.99,
    "sampleSize": 1,
    "timestamp" : startOfHour,
    "collectorId" : "collector1",
    "historicType" : "HOURS"
  },
  {
    "viewId" : "AWS/1234123/alb2",
    "identifier": "AWS/1234123/alb2",
    "appVersion" : "2",
    "platform" : "Android",
    "name" : "availabilityRate",
    "value" : 99.99,
    "sampleSize": 1,
    "timestamp" : startOfDay,
    "collectorId" : "collector1",
    "historicType" : "DAYS"
  },
  {
    "viewId" : "1234123",
    "identifier": "AWS/1234123",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "responseTime",
    "value" : 1.2,
    "sampleSize": 369.0,
    "timestamp" : startOfMinute,
    "collectorId" : "collector1",
    "historicType" : "MINUTES"
  },
  {
    "viewId" : "1234123",
    "identifier": "AWS/1234123",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "responseTime",
    "value" : 1.2,
    "sampleSize": 369.0,
    "timestamp" : startOfHour,
    "collectorId" : "collector1",
    "historicType" : "HOURS"
  },
  {
    "viewId" : "1234123",
    "identifier": "AWS/1234123",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "responseTime",
    "value" : 1.2,
    "sampleSize": 369.0,
    "timestamp" : startOfDay,
    "collectorId" : "collector1",
    "historicType" : "DAYS"
  },
  {
    "viewId" : "AWS/1234123/alb",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "requestsNumber",
    "value" : 12,
    "sampleSize": 1,
    "timestamp" : startOfMinute,
    "collectorId" : "collector1",
    "historicType" : "MINUTES"
  },
  {
    "viewId" : "AWS/1234123/alb",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "requestsNumber",
    "value" : 12,
    "sampleSize": 1,
    "timestamp" : startOfHour,
    "collectorId" : "collector1",
    "historicType" : "HOURS"
  },
  {
    "viewId" : "AWS/1234123/alb",
    "appVersion" : "2",
    "platform" : "AWS",
    "name" : "requestsNumber",
    "value" : 12,
    "sampleSize": 1,
    "timestamp" : startOfDay,
    "collectorId" : "collector1",
    "historicType" : "DAYS"
  }
]);
