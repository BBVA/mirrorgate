var now = (new Date()).getTime();
var hour = 60 * 60 * 1000;
var day = 24 * hour;
var week = 7 * day;

db.getCollection('reviews').insertMany([
  {
    "appname" : "moods",
    "starrating" : 3.5,
    "platform" : "Android",
    "amount": 20
  },
  {
    "appname" : "moods",
    "starrating" : 4.3,
    "platform" : "IOS",
    "amount": 10
  },  
  {
    "timestamp" : now - 10 * day,
    "appname" : "moods",
    "authorName" : "Kike",
    "starrating" : 3.0,
    "comment" : "Pufff!",
    "platform" : "IOS"
  },
  {
    "timestamp" : now -5 * day,
    "appname" : "moods",
    "authorName" : "Kike",
    "starrating" : 4.0,
    "comment" : "It's now beter!",
    "platform" : "IOS"
  },  
  {
    "timestamp" : now -10 * day,
    "appname" : "moods",
    "authorName" : "Kike",
    "starrating" : 5.0,
    "comment" : "Awesome!",
    "platform" : "Android"
  },  
  
  {
    "timestamp" : now - 20 * day,
    "appname" : "moods",
    "authorName" : "Ana",
    "starrating" : 4.0,
    "comment" : "Buen diseño",
    "platform" : "Android",
    "amount": 1
  },
  {
    "timestamp" : now - 3 * day,
    "appname" : "moods",
    "authorName" : "Ana",
    "starrating" : 4.0,
    "comment" : "Buen diseño",
    "platform" : "Android"
  },
  {
    "timestamp" : now - 40 * day,
    "appname" : "moods",
    "authorName" : "kike",
    "starrating" : 4.0,
    "comment" : "Consume mucho",
    "platform" : "Android"
  }
]);
