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
  var config;

  function getMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {};
      if(response.length && response.length > 0) {
        let requestsNumber =  0;
        let errorsNumber = 0;
        let availabilityRate;
        let responseTime = 0;
        let responseTimeSampleSize = 0;
        let requestsNumberTendency;
        let availabilityRateTendency;
        let responseTimeTendency;
        let infraCost = 0;
        let metricsMap = {};

        response.forEach(function(metric) {
          var metricId = metric.viewId + metric.name + metric.plarform + metric.appVerion;
          metricsMap[metricId] = metricsMap[metricId] && metricsMap[metricId] >= metric.timestamp ? metricsMap[metricId] : metric.timestamp;
          if(metric.name === 'requestsNumber') {
            requestsNumber += parseInt(metric.value);
            let requestNumberTendencyChange = parseInt(metric.longTermTendency);
            requestsNumberTendency = requestNumberTendencyChange < -10 ? 'threedown' : requestNumberTendencyChange < -5 ? 'twodown' : requestNumberTendencyChange < -1 ? 'onedown' : requestNumberTendencyChange > 10 ? 'threeup' : requestNumberTendencyChange > 5 ? 'twoup' : requestNumberTendencyChange > 1 ? 'oneup' : 'eq';
            return;
          }
          if(metric.name === 'errorsNumber') {
            errorsNumber += parseInt(metric.value);
            let errorsRateTendencyChange = parseInt(metric.midTermTendency);
            errorsRateTendency = errorsRateTendencyChange < -10 ? 'threedown-green' : errorsRateTendencyChange < -5 ? 'twodown-green' : errorsRateTendencyChange < -1 ? 'onedown-green' : errorsRateTendencyChange > 10 ? 'threeup-red' : errorsRateTendencyChange > 5 ? 'twoup-red' : errorsRateTendencyChange > 1 ? 'oneup-red' : 'eq';
            return;
          }
          if(metric.name === 'availabilityRate') {
            if(metric.sampleSize) {
              availabilityRate =  metric.value / metric.sampleSize;
              let availabilityRateTendencyChange = parseInt(metric.midTermTendency);
              availabilityRateTendency = availabilityRateTendencyChange < -10 ? 'threedown' : availabilityRateTendencyChange < -5 ? 'twodown' : availabilityRateTendencyChange < -1 ? 'onedown' : availabilityRateTendencyChange > 10 ? 'threeup' : availabilityRateTendencyChange > 5 ? 'twoup' : availabilityRateTendencyChange > 1 ? 'oneup' : 'eq';
            }
            return;
          }
          if(metric.name === 'responseTime') {
            if(metric.sampleSize) {
              responseTime = metric.value / metric.sampleSize;
              let responseTimeTendencyChange = parseInt(metric.midTermTendency);
              responseTimeTendency = responseTimeTendencyChange < -10 ? 'threedown-green' : responseTimeTendencyChange < -5 ? 'twodown-green' : responseTimeTendencyChange < -1 ? 'onedown-green' : responseTimeTendencyChange > 10 ? 'threeup-red' : responseTimeTendencyChange > 5 ? 'twoup-red' : responseTimeTendencyChange > 1 ? 'oneup-red' : 'eq';
            }
            return;
          }
          if(metric.name === 'infrastructureCost' && metric.timestamp === metricsMap[metricId]) {
            infraCost += parseFloat(metric.value);
            return;
          }

        }, this);

        let errorsRate = requestsNumber ? (100 * parseFloat(errorsNumber / requestsNumber).toFixed(2)) : undefined;

        model.metrics = {
          errorsRate: errorsRate,
          availabilityRate: availabilityRate && parseFloat(availabilityRate.toFixed(2)),
          responseTime: responseTime && parseFloat(responseTime.toFixed(2)),
          requestsNumber: requestsNumber,
          requestsNumberTendency: requestsNumberTendency,
          availabilityRateTendency: availabilityRateTendency,
          responseTimeTendency: responseTimeTendency,
          errorsRateTendency: errorsRateTendency,
          infraCost: infraCost && parseFloat(infraCost.toFixed(2))
        };

        model.responseTimeAlertingLevels = {
          warning: config.responseTimeAlertingLevelWarning,
          error: config.responseTimeAlertingLevelError
        };

        model.errorsRateAlertingLevels = {
          warning: config.errorsRateAlertingLevelWarning,
          error: config.errorsRateAlertingLevelError
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
  this.init = function(_config) {
    config = _config;
    if(!config.analyticViews || !config.analyticViews.length) {
      return Promise.reject();
    }
    service.addListener(getMetrics);
  };

});
