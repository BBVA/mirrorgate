var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "only-branch",
    "repoName": "only-branch",
    "branch": "branch",
    "keywords": [
        "http://fake.url/1",
        "only-branch",
        "only-branch"
    ]
  }
]);
