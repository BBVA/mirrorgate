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

  function getUserMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {
        rtActiveUsers: 0,
        weekUsersCount: 0
      };

      response.forEach(function(metric) {
        model.rtActiveUsers += metric.rtActiveUsers;
        model.weekUsersCount += metric.weekUsersCount;
      }, this);
    }

    observable.notify(model);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getUserMetrics);
  };
  this.init = function() {
    service.addListener(getUserMetrics);
  };

});
