var now = new Date();
var startOfMinute = parseInt((new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes())).getTime());
var startOfHour = parseInt((new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours())).getTime());
var currentTimestamp = parseInt(now.getTime());

db.getCollection('commits').insertMany([
  {
    "id" : "1234123",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : startOfHour,
    "branches" : {
      "refs/remotes/origin/master": currentTimestamp
    }
  },
  {
    "id" : "1234124",
    "repository": "ssh://git@fake.com:fake/repo1.git",
    "timestamp" : startOfHour,
    "branches" : {
      "refs/remotes/origin/master": currentTimestamp
    }
  }
]);
