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
 * SCMMetricsController - Controller to display Time to market
 *
 */
var SCMMetricsController = (function(dashboardId) {

  var observable = new Event('SCMMetricsController');
  var service = Service.get(Service.types.scmMetrics, dashboardId);

  function getSCMMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {
        timeToMaster: 0
      };

      response.forEach(function(metric) {
          model.timeToMaster
        }, this);

    }

    observable.notify(model);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getSCMMetrics);
  };
  this.init = function(config) {
    if(config.boards && config.boards.length) {
      service.addListener(getSCMMetrics);
    } else {
      return Promise.reject();
    }
  };

});
