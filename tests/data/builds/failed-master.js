var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/failed-master/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "failed-master",
    "repoName": "failed-master",
    "branch": "master",
    "keywords": [
        "http://fake.url/failed-master/1",
        "failed-master",
        "failed-master"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/failed-master/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "failed-master",
    "repoName": "failed-master",
    "branch": "develop",
    "keywords": [
        "http://fake.url/failed-master/2",
        "failed-master",
        "failed-master"
    ]
  }
]);
