db.getCollection('dashboards').insertMany([
    {
        "name":"mirrorgate",
        "logoUrl":"/mirrorgate/img/logo.png",
        "type" : "Custom",
        "columns":[
          [ "bugs", "current-sprint", "next-sprint" ],
          [ "scm-metrics", "builds", "buildsstats" ],
          [ "program-increment"],
          [ "markets", "reviews", "user-metrics", "operations-metrics" ]
        ],
        "codeRepos":[
            "mirrorgate-app",
            "MirrorGate",
            "jira-collector",
            "design"
        ],
        "applications":[],
        "boards":[
            "MirrorGate"
        ],
        'programIncrement': 'MG[0-9]{2}',
        "analyticViews": ["1234123"],
        "operationViews": ["1234123"],
        "gitRepos": [
          "ssh://git@fake.com:fake/repo1.git"
        ]
    },
    {
        "name":"all-the-stuff",
        "displayName":"All the indicators",
        "logoUrl":"/mirrorgate/img/logo.png",
        "codeRepos":[
            "mirrorgate-app",
            "MirrorGate",
            "jira-collector",
            "design"
        ],
        "columns": [
            ["scm-metrics","current-sprint","bugs"],
            ["program-increment","next-sprint"],
            ["builds","buildsstats"],
            ["markets","reviews","user-metrics","operations-metrics"],
            ["alerts"]
        ],
        "applications":['moods'],
        "boards":[
            "MirrorGate"
        ],
        'programIncrement': 'MG02',
        "slackTeam": "All",
        "urlAlerts": "alerts",
        "analyticViews": ["1234123"],
        "operationViews": ["123412"]
    },
    {
        "name":"program-increment",
        "displayName":"Program Increment",
        "codeRepos":[
            "mirrorgate-app",
            "MirrorGate",
            "jira-collector",
            "design"
        ],
        "boards":[
            "MirrorGate2"
        ],
        'programIncrement': '2017_PI03'
    },
    {
        "name":"nothing"
    },
    {
        "name":"empty",
        "codeRepos":[],
        "applications":[],
        "boards":[]
    },
    {
        "name":"all",
        "codeRepos":[".*"],
        "applications":[],
        "boards":[]
    },
    {
        "name":"mood",
        "applications":["Mood"]
    },
    {
        "name" : "aggregated",
        "type" : "Aggregate",
        "skin" : "classic",
        "aggregatedDashboards" : [
            "mirrorgate",
            "all-the-stuff",
            "nothing",
            "empty",
            "all"
        ]
    }
]);
