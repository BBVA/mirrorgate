db.getCollection('commits').insertMany([
  {
    "id" : "1234123",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : 1518009865,
    "branches" : {
      "refs/remotes/origin/master": 1519231554
    }
  },
  {
    "id" : "1234124",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : 1518009865,
    "branches" : {
      "refs/remotes/origin/master": 1519231554
    }
  }
]);
