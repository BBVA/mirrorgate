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
 * RequestsController
 *
 */
var RequestsController = (function(dashboardId) {

  var observable = new Event('RequestsController');
  var service = Service.get(Service.types.userMetrics, dashboardId);

  function getMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {};
      if(response.length && response.length > 0) {
        model.metrics = {
          requestsNumber: 0,
          errorsNumber: 0
        };

        response.forEach(function(metric) {
          if(metric.name === 'requestsNumber') {
            model.metrics.requestsNumber += parseInt(metric.value);
          }
          if(metric.name === 'errorsNumber') {
            model.metrics.errorsNumber += parseInt(metric.value);
          }
        }, this);

      }
    }

    observable.notify(model);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getMetrics);
  };
  this.init = function() {
    service.addListener(getMetrics);
  };

});
