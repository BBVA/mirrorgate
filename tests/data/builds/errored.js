var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": "aaa",
    "number": null,
    "buildUrl": "http://fake.url/errored/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 0,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "errored",
    "repoName": "errored",
    "branch": "master",
    "keywords": [
        "http://fake.url/errored/2",
        "errored",
        "errored"
    ]
  }
]);
