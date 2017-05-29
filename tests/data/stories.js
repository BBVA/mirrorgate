
var builds = listFiles('data/stories');

for(var i in builds) {
    load(builds[i].name);
    var name = builds[i].baseName;
    name = name.substring(0, name.length -3);
    db.getCollection('dashboards').insertOne({
        "name":name,
        "boards":[
            name
        ]
    });
};
