
var metrics = listFiles('data/historic_user_metrics');

for(var i in metrics) {
    load(metrics[i].name);
}
