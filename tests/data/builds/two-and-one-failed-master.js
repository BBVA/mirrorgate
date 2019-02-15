var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/two-and-one-failed-master/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "two-and-one-failed-master",
    "repoName": "failed",
    "branch": "master",
    "keywords": [
        "http://fake.url/two-and-one-failed-master/1",
        "two-and-one-failed-master",
        "failed"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/two-and-one-failed-master/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "two-and-one-failed-master",
    "repoName": "ok",
    "branch": "master",
    "keywords": [
        "http://fake.url/two-and-one-failed-master/2",
        "two-and-one-failed-master",
        "ok"
    ]
  }
]);
