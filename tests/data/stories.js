
var stories = listFiles('data/stories');

for(var i in stories) {
    load(stories[i].name);
    var name = stories[i].baseName;
    name = name.substring(0, name.length -3);
    db.getCollection('dashboards').insertOne({
        "name":name,
        "boards":[
            name
        ]
    });
}
