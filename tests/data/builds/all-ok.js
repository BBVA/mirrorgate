var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/all-ok/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "all-ok",
    "repoName": "all-ok",
    "branch": "master",
    "keywords": [
        "http://fake.url/all-ok/1",
        "all-ok",
        "all-ok"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/all-ok/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "all-ok",
    "repoName": "all-ok",
    "branch": "develop",
    "keywords": [
        "http://fake.url/all-ok/2",
        "all-ok",
        "all-ok"
    ]
  }
]);
