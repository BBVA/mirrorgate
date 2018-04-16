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

        var metricsMap = {};

        response.forEach(function(metric) {
          if(metricsMap[metric.name] === undefined) {
            metricsMap[metric.name] = {
              oneDayValue: 0,
              oneDaySampleSize: 0,
              sevenDaysValue: 0,
              sevenDaysSampleSize: 0,
              tendency: undefined
            };
          }

          metricsMap[metric.name].oneDayValue = metric.oneDayValue ? metricsMap[metric.name].oneDayValue + metric.oneDayValue : metricsMap[metric.name].oneDayValue;
          metricsMap[metric.name].oneDaySampleSize = metric.oneDaySampleSize ? metricsMap[metric.name].oneDaySampleSize + metric.oneDaySampleSize : metricsMap[metric.name].oneDaySampleSize;
          metricsMap[metric.name].sevenDaysValue = metric.sevenDaysValue ? metricsMap[metric.name].sevenDaysValue + metric.sevenDaysValue : metricsMap[metric.name].sevenDaysValue;
          metricsMap[metric.name].sevenDaysSampleSize = metric.sevenDaysSampleSize ? metricsMap[metric.name].sevenDaysSampleSize + metric.sevenDaysSampleSize : metricsMap[metric.name].sevenDaysSampleSize;
        });

        for (var metric in metricsMap) {
          var tendencyChange = Utils.getPercentageDifference(metricsMap[metric].sevenDaysValue / metricsMap[metric].sevenDaysSampleSize || 0, metricsMap[metric].oneDayValue / metricsMap[metric].oneDaySampleSize || 0);
          if(metric === 'errorsNumber' || metric === 'responseTime' || metric === 'infrastructureCost' ) {
            metricsMap[metric].tendency = tendencyChange < -10 ? 'threedown-green' : tendencyChange < -5 ? 'twodown-green' : tendencyChange < -1 ? 'onedown-green' : tendencyChange > 10 ? 'threeup-red' : tendencyChange > 5 ? 'twoup-red' : tendencyChange > 1 ? 'oneup-red' : 'eq';
          } else {
            metricsMap[metric].tendency = tendencyChange < -10 ? 'threedown' : tendencyChange < -5 ? 'twodown' : tendencyChange < -1 ? 'onedown' : tendencyChange > 10 ? 'threeup' : tendencyChange > 5 ? 'twoup' : tendencyChange > 1 ? 'oneup' : 'eq';
          }
        }

        model.metrics = {
          requestsNumber: metricsMap.requestsNumber && metricsMap.requestsNumber.oneDayValue,
          requestsNumberTendency: metricsMap.requestsNumber && metricsMap.requestsNumber.tendency,
          errorsRate: metricsMap.errorsNumber && metricsMap.errorsNumber.oneDaySampleSize > 0 ? parseFloat((100 * metricsMap.errorsNumber.oneDayValue / metricsMap.errorsNumber.oneDaySampleSize || 0).toFixed(2)) : undefined,
          errorsRateTendency: metricsMap.errorsNumber && metricsMap.errorsNumber.oneDaySampleSize > 0 && metricsMap.errorsNumber.tendency,
          availabilityRate: metricsMap.availabilityRate && parseFloat((metricsMap.availabilityRate.oneDayValue / metricsMap.availabilityRate.oneDaySampleSize).toFixed(2)),
          availabilityRateTendency: metricsMap.availabilityRate && metricsMap.availabilityRate.tendency,
          responseTime: metricsMap.responseTime && metricsMap.responseTime.oneDaySampleSize > 0 ? parseFloat((metricsMap.responseTime.oneDayValue / metricsMap.responseTime.oneDaySampleSize).toFixed(2)) : undefined,
          responseTimeTendency: metricsMap.responseTime && metricsMap.responseTime.oneDaySampleSize > 0 ? metricsMap.responseTime.tendency : undefined,
          infraCost: metricsMap.infrastructureCost && parseFloat(metricsMap.infrastructureCost.oneDayValue.toFixed(2)),
          infraCostTendency: metricsMap.infrastructureCost && metricsMap.infrastructureCost.tendency
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
