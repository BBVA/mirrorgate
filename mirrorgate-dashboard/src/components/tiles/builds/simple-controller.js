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
 * SimpleBuildsController - Controller to handle simple builds component
 *
 */
var SimpleBuildsController = (function(dashboardId) {

  var observable = new Event('SimpleBuildsController');
  var service = Service.get(Service.types.builds, dashboardId);

  function getBuildsStats(response) {
    var data;
    if (response) {
      response = JSON.parse(response);

      if (response.lastBuilds) {

        data = {stats: response.stats};
        data.stats.lastBuildTimestamp = 0;
        data.masterBuildsCount = 0;
        data.failedMasterBuildsCount = 0;
        data.unstableMasterBuildsCount = 0;
        data.developBuildsCount = 0;
        data.failedDevelopBuildsCount = 0;
        data.unstableDevelopBuildsCount = 0;

        for (var index in response.lastBuilds) {

          var build = response.lastBuilds[index];
          switch (build.branch || 'master') {
            case 'master':
              data.masterBuildsCount++;
              if(build.buildStatus === 'Failure') {
                data.failedMasterBuildsCount++;
              } else if(build.buildStatus === 'Unstable') {
                data.unstableMasterBuildsCount++;
              }
              break;
            case 'develop':
              data.developBuildsCount++;
              if(build.buildStatus === 'Failure') {
                data.failedDevelopBuildsCount++;
              } else if(build.buildStatus === 'Unstable') {
                data.unstableDevelopBuildsCount++;
              }
              break;
          }

          if(build.timestamp > data.stats.lastBuildTimestamp) {
            data.stats.lastBuildTimestamp = build.timestamp;
          }
        }

        data.stats.failureRate = data.stats.failureRate >= 0 ? parseFloat(data.stats.failureRate.toFixed(1)) : undefined;

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
