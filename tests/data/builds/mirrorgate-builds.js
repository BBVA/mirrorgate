var now = (new Date()).getTime() - (1000 * 60 * 60 * 2);

db.getCollection('builds').insertMany([
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 12340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "develop",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
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
         "http://fake.url",
         "MirrorGate",
         "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 2435340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-12",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 23340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-10",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
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
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 12323410,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "refactor/APINaming",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 123410,
    "buildStatus": "Unstable",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "chore/Perceptual-testing",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
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
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 5647430,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "WIP-Refactor-D3",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452230,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "jira-collector",
    "branch": "develop",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "jira-collector"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "design",
    "branch": "master",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "design"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452320,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "develop",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 234520,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "PR-8",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 234520,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-app",
    "branch": "feature/RegexBuilds",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-app"
    ]
  },
  {
    "timestamp": now,
    "number": null,
    "buildUrl": "http://fake.url",
    "startTime": 0,
    "endTime": 0,
    "duration": 23452340,
    "buildStatus": "Success",
    "startedBy": null,
    "projectName": "MirrorGate",
    "repoName": "mirrorgate-jenkins-plugin",
    "branch": "master",
    "keywords": [
        "http://fake.url",
        "MirrorGate",
        "mirrorgate-jenkins-plugin"
    ]
  }
]);