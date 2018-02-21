
var commits = listFiles('data/scm-metrics');

for(var i in commits) {
    load(commits[i].name);
}
