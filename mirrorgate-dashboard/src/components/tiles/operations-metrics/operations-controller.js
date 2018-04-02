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
        var requestsNumber =  0;
        var errorsNumber = 0;
        var availabilityRate = 0;
        var availabilityRateSampleSize = 0;
        var responseTime = 0;
        var responseTimeSampleSize = 0;
        var requestsNumberTendency;
        var errorsRateTendency;
        var availabilityRateTendency;
        var responseTimeTendency;
        var infraCost = 0;
        var metricsMap = {};

        response.forEach(function(metric) {
          var metricId = metric.viewId + metric.name + metric.plarform + metric.appVerion;
          metricsMap[metricId] = metricsMap[metricId] && metricsMap[metricId] >= metric.timestamp ? metricsMap[metricId] : metric.timestamp;
          if(metric.name === 'requestsNumber') {
            requestsNumber += parseInt(metric.value);
            var requestNumberTendencyChange = parseInt(metric.longTermTendency);
            requestsNumberTendency = requestNumberTendencyChange < -10 ? 'threedown' : requestNumberTendencyChange < -5 ? 'twodown' : requestNumberTendencyChange < -1 ? 'onedown' : requestNumberTendencyChange > 10 ? 'threeup' : requestNumberTendencyChange > 5 ? 'twoup' : requestNumberTendencyChange > 1 ? 'oneup' : 'eq';
            return;
          }
          if(metric.name === 'errorsNumber') {
            errorsNumber += parseInt(metric.value);
            var errorsRateTendencyChange = parseInt(metric.midTermTendency);
            errorsRateTendency = errorsRateTendencyChange < -10 ? 'threedown-green' : errorsRateTendencyChange < -5 ? 'twodown-green' : errorsRateTendencyChange < -1 ? 'onedown-green' : errorsRateTendencyChange > 10 ? 'threeup-red' : errorsRateTendencyChange > 5 ? 'twoup-red' : errorsRateTendencyChange > 1 ? 'oneup-red' : 'eq';
            return;
          }
          if(metric.name === 'availabilityRate') {
            availabilityRate +=  metric.value * metric.sampleSize || 1;
            availabilityRateSampleSize += metric.sampleSize || 1;
            var availabilityRateTendencyChange = parseInt(metric.midTermTendency);
            availabilityRateTendency = availabilityRateTendencyChange < -10 ? 'threedown' : availabilityRateTendencyChange < -5 ? 'twodown' : availabilityRateTendencyChange < -1 ? 'onedown' : availabilityRateTendencyChange > 10 ? 'threeup' : availabilityRateTendencyChange > 5 ? 'twoup' : availabilityRateTendencyChange > 1 ? 'oneup' : 'eq';
            return;
          }
          if(metric.name === 'responseTime') {
            responseTime += metric.value * metric.sampleSize || 1;
            responseTimeSampleSize += metric.sampleSize || 1;
            var responseTimeTendencyChange = parseInt(metric.midTermTendency);
            responseTimeTendency = responseTimeTendencyChange < -10 ? 'threedown-green' : responseTimeTendencyChange < -5 ? 'twodown-green' : responseTimeTendencyChange < -1 ? 'onedown-green' : responseTimeTendencyChange > 10 ? 'threeup-red' : responseTimeTendencyChange > 5 ? 'twoup-red' : responseTimeTendencyChange > 1 ? 'oneup-red' : 'eq';
            return;
          }
          if(metric.name === 'infrastructureCost' && metric.timestamp === metricsMap[metricId]) {
            infraCost += parseFloat(metric.value);
            return;
          }

        }, this);

        var errorsRate = requestsNumber ? (100 * parseFloat(errorsNumber / requestsNumber).toFixed(2)) : undefined;

        model.metrics = {
          errorsRate: errorsRate,
          availabilityRate: availabilityRate && parseFloat(availabilityRate / availabilityRateSampleSize).toFixed(2),
          responseTime: responseTime && parseFloat(responseTime / responseTimeSampleSize).toFixed(2),
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
    // TO-DO remove analyticViews check
    if(!((config.analyticViews && config.analyticViews.length) || (config.operationViews && config.operationViews.length))) {
      return Promise.reject();
    }
    service.addListener(getMetrics);
  };

});
