var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/1",
    "startTime": 0,
    "endTime": 0,
    "duration": 12340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "develop",
    "keywords": [
        "http://fake.url/mirrorgate/1",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/2",
    "startTime": 0,
    "endTime": 0,
    "duration": 4532350,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "PR-7",
    "culprits": ["Gmork"],
    "keywords": [
         "http://fake.url/mirrorgate/2",
         "MirrorGate",
         "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/3",
    "startTime": 0,
    "endTime": 0,
    "duration": 2435340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-12",
    "keywords": [
        "http://fake.url/mirrorgate/3",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/4",
    "startTime": 0,
    "endTime": 0,
    "duration": 23340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-10",
    "keywords": [
        "http://fake.url/mirrorgate/4",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/5",
    "startTime": 0,
    "endTime": 0,
    "duration": 3420,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "refactor/APINaming",
    "culprits": ["gmork"],
    "keywords": [
        "http://fake.url/mirrorgate/5",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/6",
    "startTime": 0,
    "endTime": 0,
    "duration": 12323410,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "refactor/APINaming",
    "keywords": [
        "http://fake.url/mirrorgate/6",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/7",
    "startTime": 0,
    "endTime": 0,
    "duration": 123410,
    "buildStatus": "Unstable",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "chore/Perceptual-testing",
    "keywords": [
        "http://fake.url/mirrorgate/7",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/8",
    "startTime": 0,
    "endTime": 0,
    "duration": 234520,
    "buildStatus": "Failure",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "feature/DOST05-128-docs-add-tool-documentation",
    "culprits": ["Gmork", "Xayide"],
    "keywords": [
        "http://fake.url/mirrorgate/8",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/9",
    "startTime": 0,
    "endTime": 0,
    "duration": 5647430,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "WIP-Refactor-D3",
    "keywords": [
        "http://fake.url/mirrorgate/9",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/10",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452230,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "jira-collector",
    "branch": "develop",
    "keywords": [
        "http://fake.url/mirrorgate/10",
        "MirrorGate",
        "jira-collector"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/11",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "design",
    "branch": "master",
    "keywords": [
        "http://fake.url/mirrorgate/11",
        "MirrorGate",
        "design"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/12",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452320,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "develop",
    "keywords": [
        "http://fake.url/mirrorgate/12",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/13",
    "startTime": 0,
    "endTime": 0,
    "duration": 234520,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-8",
    "keywords": [
        "http://fake.url/mirrorgate/13",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/14",
    "startTime": 0,
    "endTime": 0,
    "duration": 234520,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "feature/RegexBuilds",
    "keywords": [
        "http://fake.url/mirrorgate/14",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url/mirrorgate/15",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "master",
    "keywords": [
        "http://fake.url/mirrorgate/15",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  }
]);