
var metrics = listFiles('data/user-metrics');

for(var i in metrics) {
    load(metrics[i].name);
}
