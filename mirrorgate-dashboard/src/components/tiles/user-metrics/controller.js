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

  var observable = new Event('UserMetricsController');
  var service = Service.get(Service.types.userMetrics, dashboardId);
  var _config;

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

        var last_versions = {};
        var lastVersionActiveUsers;

        response.forEach(function(metric) {
          if(metric.name === 'activeUsers') {
            model.metrics.rtActiveUsers += parseInt(metric.value);
            if(!metric.appVersion || !metric.appVersion.match(_lastVersion)) {
              return;
            } else if (metric.appVersion === last_versions[metric.viewId + (metric.platform || '')]) {
              lastVersionActiveUsers += parseInt(metric.value);
            } else {
              if(!last_versions[metric.viewId + (metric.platform || '')] || Utils.compareVersions(metric.appVersion, last_versions[metric.viewId + (metric.platform || '')], _lastVersion) > 0) {
                last_versions[metric.viewId + (metric.platform || '')] = metric.appVersion;
                lastVersionActiveUsers = lastVersionActiveUsers || 0 + parseInt(metric.value);
              }
            }
          }
          if(metric.name === '7dayUsers') {
            model.metrics.sevenDayUsers += parseInt(metric.value);
          }
        }, this);

        model.metrics.lastVersionActiveUsersRate = lastVersionActiveUsers !== undefined  ? parseFloat((100 * lastVersionActiveUsers / model.metrics.rtActiveUsers).toFixed(2)) : undefined;
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