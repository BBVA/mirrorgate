var conn = new Mongo();
var db = conn.getDB('dashboarddb');

var purgeDate = new Date(new Date().setMonth(new Date().getMonth() - 3));

'Removing old builds until: ' + purgeDate;

db.getCollection('builds').remove({
  timestamp: {'$lt' : purgeDate.getTime()},
  latest: false
});

