var conn = new Mongo();
var db = conn.getDB('dashboarddb');

var purgeDate = new Date(new Date().setDate(new Date().getDate() - 1));

'Removing old events until: ' + purgeDate;

db.getCollection('events').remove({
  timestamp: {'$lt' : purgeDate.getTime()},
});
