
var reviews = listFiles('data/reviews');

for(var i in reviews) {
    load(reviews[i].name);
    var name = reviews[i].baseName;
    name = name.substring(0, name.length -3);
    db.getCollection('dashboards').insertOne({
        "name":name,
        "applications":[
          name
        ]
    });
}
