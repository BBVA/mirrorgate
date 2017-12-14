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
 * BuildsStatsController - Controller to handle the builds stats
 *
 */

var BuildsStatsController = (function(dashboardId) {

  var observable = new Event('BuildsStatsController');
  var service = Service.get(Service.types.builds, dashboardId);
  var config;

  function getBuildsStats(response) {
    var data;
    if (response) {
      response = JSON.parse(response);

      if (response.lastBuilds) {
        data = {stats: response.stats, buildRoot: []};
        data.stats.lastBuildTimestamp = 0;

        for (var index in response.lastBuilds) {
          var item = response.lastBuilds[index];

          if(item.timestamp > data.stats.lastBuildTimestamp) {
            data.stats.lastBuildTimestamp = item.timestamp;
          }
        }

        data.stats.failureRate = data.stats.failureRate ? parseFloat(data.stats.failureRate.toFixed(1)) : undefined;
      }
    }

    observable.notify(data);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getBuildsStats);
  };
  this.init = function(config) {
    if(!config.codeRepos || !config.codeRepos.length) {
      return Promise.reject();
    }

    service.addListener(getBuildsStats);
  };

});
