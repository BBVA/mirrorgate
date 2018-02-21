var now = (new Date()).getTime();

db.getCollection('commits').insertMany([
  {
    "id" : "1234123",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : now,
    "branches" : {
      "ref": "1518009865"
    }
  },
  {
    "id" : "1234124",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : now,
    "branches" : {
      "collector1": "1518009865"
    }
  }
]);
