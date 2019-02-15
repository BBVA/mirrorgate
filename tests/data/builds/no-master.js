var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/no-master/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "no-master",
    "repoName": "no-master",
    "branch": "branch",
    "keywords": [
        "http://fake.url/no-master/1",
        "no-master",
        "no-master"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/no-master/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "no-master",
    "repoName": "no-master",
    "branch": "develop",
    "keywords": [
        "http://fake.url/no-master/2",
        "no-master",
        "no-master"
    ]
  }
]);
