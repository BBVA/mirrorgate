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
 * UserMetricsController - Controller to handle user metric data
 *
 */
var UserMetricsController = (function(dashboardId) {
  'user strict';

  var observable = new Event('UserMetricsController');
  var service = Service.get(Service.types.userMetrics, dashboardId);
  var _config;
  var _lastVersion;

  function getUserMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {};
      if(response.length && response.length > 0) {
        model.metrics = {
          rtActiveUsers: 0,
          sevenDayUsers: 0
        };

        var versions = [];
        var versionsMap = {};
        var metricsMap = {};
        let sevenDayUsersTendency;
        let activeUsersTendency;

        response.forEach(function(metric) {
          var metricId = metric.viewId + metric.name + metric.plarform + metric.appVerion;
          metricsMap[metricId] = metricsMap[metricId] && metricsMap[metricId] >= metric.timestamp ? metricsMap[metricId] : metric.timestamp;
          if(metric.name === 'activeUsers' && metric.timestamp === metricsMap[metricId]) {
            model.metrics.rtActiveUsers += parseInt(metric.value);
            if(metric.appVersion && metric.appVersion.match(_lastVersion)) {
              var value = parseInt(metric.value);
              var name = Utils.rephraseVersion(metric.appVersion, _lastVersion);
              var versionData = versionsMap[name];

              if(!versionData) {
                versionData = versionsMap[name] = {
                  value: 0,
                  versions: {},
                  cannonical: metric.appVersion,
                  name: name
                };
                versions.push(versionData);
              }
              versionData.versions[metric.appVersion] = metric.appVersion;
              versionData.value += value;
              let activeUsersTendencyChange = parseInt(metric.shortTermTendency);
              activeUsersTendency = activeUsersTendencyChange < -10 ? 'threedown' : activeUsersTendencyChange < -5 ? 'twodown' : activeUsersTendencyChange < -1 ? 'onedown' : activeUsersTendencyChange > 10 ? 'threeup' : activeUsersTendencyChange > 5 ? 'twoup' : activeUsersTendencyChange > 1 ? 'oneup' : 'eq';
            }
          } else if(metric.name === '7dayUsers' && metric.timestamp === metricsMap[metricId]) {
            model.metrics.sevenDayUsers += parseInt(metric.value);
            let sevenDaysUsersTendencyChange = parseInt(metric.longTermTendency);
            sevenDayUsersTendency = sevenDaysUsersTendencyChange < -10 ? 'threedown' : sevenDaysUsersTendencyChange < -5 ? 'twodown' : sevenDaysUsersTendencyChange < -1 ? 'onedown' : sevenDaysUsersTendencyChange > 10 ? 'threeup' : sevenDaysUsersTendencyChange > 5 ? 'twoup' : sevenDaysUsersTendencyChange > 1 ? 'oneup' : 'eq';
          }
        }, this);

        versions = versions.sort(function(a,b) {
          return - Utils.compareVersions(a.cannonical, b.cannonical, _lastVersion);
        });

        model.metrics.lastVersionActiveUsersRate = versions.length > 0 ? parseFloat((100 * versions[0].value / model.metrics.rtActiveUsers).toFixed(2)) : undefined;
        model.metrics.versions = versions.length && versions;
        model.metrics.sevenDayUsersTendency = sevenDayUsersTendency;
      }
    }

    observable.notify(model);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getUserMetrics);
  };
  this.init = function(config) {
    if(!config.analyticViews || !config.analyticViews.length) {
      return Promise.reject();
    }
    _lastVersion = new RegExp(config.lastVersion);
    service.addListener(getUserMetrics);
  };

});
