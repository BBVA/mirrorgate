var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/dynamic/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "dynamic",
    "repoName": "dynamic",
    "branch": "master",
    "keywords": [
        "http://fake.url/dynamic/1",
        "dynamic",
        "dynamic"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/dynamic/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "dynamic",
    "repoName": "dynamic",
    "branch": "develop",
    "keywords": [
        "http://fake.url/dynamic/2",
        "dynamic",
        "dynamic"
    ]
  }
]);
