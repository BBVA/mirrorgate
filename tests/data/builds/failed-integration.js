var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/failed-integration/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "failed-integration",
    "repoName": "failed-integration",
    "branch": "master",
    "keywords": [
        "http://fake.url/failed-integration/1",
        "failed-integration",
        "failed-integration"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/failed-integration/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "failed-integration",
    "repoName": "failed-integration",
    "branch": "develop",
    "keywords": [
        "http://fake.url/failed-integration/2",
        "failed-integration",
        "failed-integration"
    ]
  }
]);
