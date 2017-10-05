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
 * OperationsController
 *
 */
var OperationsController = (function(dashboardId) {

  var observable = new Event('OperationsController');
  var service = Service.get(Service.types.userMetrics, dashboardId);

  function getMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {};
      if(response.length && response.length > 0) {
        let requestsNumber =  0;
        let errorsNumber = 0;
        let availabilityRate;

        response.forEach(function(metric) {
          if(metric.name === 'requestsNumber') {
            requestsNumber += parseInt(metric.value);
          }
          if(metric.name === 'errorsNumber') {
            errorsNumber += parseInt(metric.value);
          }
          if(metric.name === 'availabilityRate') {
            availabilityRate = availabilityRate ? Math.min(availabilityRate, parseFloat(metric.value)) : parseFloat(metric.value);
          }
        }, this);

        model.metrics = {
          requestsNumber: requestsNumber,
          errorsNumber: errorsNumber,
          errorsRate: parseFloat((100 * errorsNumber / requestsNumber).toFixed(2)),
          availabilityRate: availabilityRate
        };
      }
    }

    observable.notify(model);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getMetrics);
  };
  this.init = function(config) {
    if(!config.analyticViews || !config.analyticViews.length) {
      return Promise.reject();
    }
    service.addListener(getMetrics);
  };

});
