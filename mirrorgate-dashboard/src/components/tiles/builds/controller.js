/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * BuildsController - Controller to handle builds
 *
 */

var BuildsController = (function(dashboardId) {

  var observable = new Event('BuildsController');
  var service = Service.get(Service.types.builds, dashboardId);
  var config;

  function getLastBuilds(response) {
    var mainBranches = {};
    var developBranches = {};

    function getMainBranch(item, data) {
      var key = item.projectName + '/' + item.repoName;
      var mainBuild = mainBranches[key];
      if(!mainBuild) {
        mainBranches[key] = mainBuild = new Build(key);
        data.buildRoot.push(mainBuild);
      }
      return mainBuild;
    }
    function getDevelopBranch(item, data) {
      var key = item.projectName + '/' + item.repoName;
      var devBuild;
      var devBuilds = developBranches[key];

      if(item.branch !== null) {
        if (!devBuilds) {
          devBuilds = developBranches[key] = {};
        }
        key += '/develop';
        devBuild = devBuilds[key];
        if (!devBuild) {
          devBuild = devBuilds[key] = new Build(key);
          getMainBranch(item,data).addChild(devBuild);
        }
      }

      return devBuild;
    }


    var data;
    if (response) {
      response = JSON.parse(response);

      if (response.lastBuilds) {
        // We structure the build list in a tree.
        data = {stats: response.stats, buildRoot: []};
        data.stats.lastBuildTimestamp = 0;

        for (var index in response.lastBuilds) {
          var item = response.lastBuilds[index];
          var filters = config.filters;
          if (filters) {
            if (filters.timeSpan &&
                new Date().getTime() - item.timestamp > filters.timeSpan) {
              continue;
            }
            if (filters.status && !filters.status[item.buildStatus]) {
              continue;
            }
            if (filters.branch && !filters.branch[item.branch]) {
              continue;
            }
          }

          var key = item.projectName + '/' + item.repoName;
          if(item.branch !== null) {
            key += '/' + item.branch;
          }

          var build;

          switch (item.branch) {
            case 'master':
              getDevelopBranch(item, data);
              /* falls through */
            case null:
            case undefined:
              build = getMainBranch(item, data);
              build.data = item;
              build.status = item.buildStatus;
              break;
            case 'develop':
              build = getDevelopBranch(item,data);
              build.data = item;
              build.status = item.buildStatus;
              break;
            default:
              build = new Build(key, item.buildStatus);
              getDevelopBranch(item,data).addChild(build);
              build.data = item;
          }

          if(item.timestamp > data.stats.lastBuildTimestamp) {
            data.stats.lastBuildTimestamp = item.timestamp;
          }

          if((build.status === 'Failure' || build.status === 'Unstable') &&
                (!data.lastRelevantBuild || data.lastRelevantBuild.data.timestamp < item.timestamp)) {
            data.lastRelevantBuild = build;
          }

        }
      }
    }

    observable.notify(data);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getLastBuilds);
  };

  this.init = function(_config) {
    config = _config;
    if(!config.codeRepos || !config.codeRepos.length) {
      return Promise.reject();
    }
    service.addListener(getLastBuilds);
  };
});
