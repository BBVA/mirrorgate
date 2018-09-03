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

var OperationsUtils = (function() {
  'use strict';

  var stateMapping = {
    Ok: 0,
    Warn: 1,
    Error: 2
  };

  var eventMap = {};

  return {

    checkOperationMetricsToSendEvent : function(emitter, stats, config){
      var metricName = '';
      var thresholds = '';
      var description = '';
      var state;
      var color;

      var responseTimeAlertingLevels = {
        warning: config.responseTimeAlertingLevelWarning,
        error: config.responseTimeAlertingLevelError
      };
      var errorsRateAlertingLevels = {
        warning: config.errorsRateAlertingLevelWarning,
        error: config.errorsRateAlertingLevelError
      };
      var availabilityRateAlertingLevels = {
        warning: config.availabilityRateAlertingLevelWarning,
        error: config.availabilityRateAlertingLevelError
      };
      var responseTimeState = OperationsUtils.getMetricState(stats.responseTime, responseTimeAlertingLevels);
      var errorsRateState = OperationsUtils.getMetricState(stats.errorsRate, errorsRateAlertingLevels);
      var availabilityRateState = OperationsUtils.getMetricStateInverse(stats.availabilityRate, availabilityRateAlertingLevels, true);

      if(errorsRateState == stateMapping.Ok && responseTimeState == stateMapping.Ok && availabilityRateState == stateMapping.Ok){

        if(eventMap[emitter]){
          metricName = 'Errors rate, response time & availability rate';
          description = 'Values have returned below the thresholds (' + errorsRateAlertingLevels.warning + ', ' + responseTimeAlertingLevels.warning + ', ' + availabilityRateAlertingLevels.warning + ' )';
          state = Math.max(errorsRateState, responseTimeState, availabilityRateState);
          color = state == stateMapping.Error ? 'red' : state == stateMapping.Warn ? 'yellow' : 'blue';

          OperationsUtils.sendEvent(emitter, metricName, description, color);
        }

      } else {

        if(availabilityRateState == stateMapping.Warn || availabilityRateState == stateMapping.Error){
          metricName = 'Availability rate';
          thresholds = availabilityRateState == stateMapping.Warn ? availabilityRateAlertingLevels.warning : availabilityRateAlertingLevels.error;
          description = 'Less than the threshold (' + thresholds + ')';
        } else if(errorsRateState == stateMapping.Warn || errorsRateState == stateMapping.Error){
          metricName = 'Errors rate';
          thresholds = errorsRateState == stateMapping.Warn ? errorsRateAlertingLevels.warning : errorsRateAlertingLevels.error;
          description = 'Greater than the threshold (' + thresholds + ')';
        } else if(responseTimeState == stateMapping.Warn || responseTimeState == stateMapping.Error){
          metricName = metricName === '' ? 'Response time' : metricName + ' and response time';
          thresholds = thresholds === '' ? '' : thresholds + ' and ';
          thresholds = thresholds + (responseTimeState == stateMapping.Warn ? responseTimeAlertingLevels.warning : responseTimeAlertingLevels.error);
          description = 'Greater than the threshold (' + thresholds + ')';
        }

        state = Math.max(errorsRateState, responseTimeState, availabilityRateState);
        color = state == stateMapping.Error ? 'red' : state == stateMapping.Warn ? 'yellow' : 'blue';

        OperationsUtils.sendEvent(emitter, metricName, description, color);
      }
    },

    getMetricState: function(metric, metricRateAlertingLevels){
      return metric === undefined || metric <= metricRateAlertingLevels.warning ? stateMapping.Ok
              : metric <= metricRateAlertingLevels.error ? stateMapping.Warn
              : stateMapping.Error;
    },

    getMetricStateInverse: function(metric, metricRateAlertingLevels){
      return metric === undefined || metric >= metricRateAlertingLevels.warning ? stateMapping.Ok
              : metric >= metricRateAlertingLevels.error ? stateMapping.Warn
              : stateMapping.Error;
    },

    getComponentState: function(metrics, responseTimeAlertingLevels, errorsRateAlertingLevels, availabilityRateAlertingLevels){
      return Math.max(OperationsUtils.getMetricState(metrics.responseTime, responseTimeAlertingLevels), OperationsUtils.getMetricState(metrics.errorsRate, errorsRateAlertingLevels), OperationsUtils.getMetricStateInverse(metrics.availabilityRate, availabilityRateAlertingLevels));
    },

    getStats: function (metrics, infraCost) {
      var metricsMap = {};

      metrics.forEach(function(metric) {
        if(metricsMap[metric.name] === undefined) {
          metricsMap[metric.name] = {
            oneDayValue: 0,
            oneDaySampleSize: 0,
            sevenDaysValue: 0,
            sevenDaysSampleSize: 0,
            tendency: undefined
          };
        }

        metricsMap[metric.name].oneDayValue = metric.oneDayValue ? metricsMap[metric.name].oneDayValue + parseFloat(metric.oneDayValue) : parseFloat(metricsMap[metric.name].oneDayValue);
        metricsMap[metric.name].oneDaySampleSize = metric.oneDaySampleSize ? metricsMap[metric.name].oneDaySampleSize + parseInt(metric.oneDaySampleSize) : parseInt(metricsMap[metric.name].oneDaySampleSize);
        metricsMap[metric.name].sevenDaysValue = metric.sevenDaysValue ? metricsMap[metric.name].sevenDaysValue + parseFloat(metric.sevenDaysValue) : parseFloat(metricsMap[metric.name].sevenDaysValue);
        metricsMap[metric.name].sevenDaysSampleSize = metric.sevenDaysSampleSize ? metricsMap[metric.name].sevenDaysSampleSize + parseInt(metric.sevenDaysSampleSize) : parseInt(metricsMap[metric.name].sevenDaysSampleSize);
      });

      for (var metric in metricsMap) {
        var tendencyChange = metricsMap[metric].oneDaySampleSize ? Utils.getPercentageDifference(metricsMap[metric].sevenDaysValue / metricsMap[metric].sevenDaysSampleSize || 0, metricsMap[metric].oneDayValue / metricsMap[metric].oneDaySampleSize || 0) : undefined;
        if(tendencyChange !== undefined) {
          if(metric === 'errorsNumber' || metric === 'responseTime' || metric === 'infrastructureCost' ) {
            metricsMap[metric].tendency = tendencyChange < -10 ? 'threedown-green' : tendencyChange < -5 ? 'twodown-green' : tendencyChange < -1 ? 'onedown-green' : tendencyChange > 10 ? 'threeup-red' : tendencyChange > 5 ? 'twoup-red' : tendencyChange > 1 ? 'oneup-red' : 'eq';
          } else {
            metricsMap[metric].tendency = tendencyChange < -10 ? 'threedown' : tendencyChange < -5 ? 'twodown' : tendencyChange < -1 ? 'onedown' : tendencyChange > 10 ? 'threeup' : tendencyChange > 5 ? 'twoup' : tendencyChange > 1 ? 'oneup' : 'eq';
          }
        }
      }

      var stats = {
        requestsNumber: metricsMap.requestsNumber && metricsMap.requestsNumber.oneDaySampleSize > 0 ? metricsMap.requestsNumber.oneDayValue : undefined,
        requestsNumberTendency: metricsMap.requestsNumber && metricsMap.requestsNumber.oneDaySampleSize > 0 ? metricsMap.requestsNumber.tendency : undefined,
        errorsRate: metricsMap.errorsNumber && metricsMap.requestsNumber && metricsMap.requestsNumber.oneDayValue > 0 ? parseFloat((100 * metricsMap.errorsNumber.oneDayValue / metricsMap.requestsNumber.oneDayValue || 0).toFixed(2)) : undefined,
        errorsRateTendency: metricsMap.errorsNumber && metricsMap.errorsNumber.oneDaySampleSize > 0 ? metricsMap.errorsNumber.tendency : undefined,
        availabilityRate: metricsMap.availabilityRate && metricsMap.availabilityRate.oneDaySampleSize > 0 ? parseFloat((metricsMap.availabilityRate.oneDayValue / metricsMap.availabilityRate.oneDaySampleSize).toFixed(2)) : undefined,
        availabilityRateTendency: metricsMap.availabilityRate &&  metricsMap.availabilityRate.oneDaySampleSize > 0 ? metricsMap.availabilityRate.tendency : undefined,
        responseTime: metricsMap.responseTime && metricsMap.responseTime.oneDaySampleSize > 0 ? parseFloat((metricsMap.responseTime.oneDayValue / metricsMap.responseTime.oneDaySampleSize).toFixed(2)) : undefined,
        responseTimeTendency: metricsMap.responseTime && metricsMap.responseTime.oneDaySampleSize > 0 ? metricsMap.responseTime.tendency : undefined,
        infraCost: infraCost && metricsMap.infrastructureCost ? parseFloat(metricsMap.infrastructureCost.oneDayValue.toFixed(2)) : undefined,
        infraCostTendency: infraCost && metricsMap.infrastructureCost ? metricsMap.infrastructureCost.tendency : undefined
      };

      return Object.values(stats).some(function(metric) {
        return metric !== undefined;
      }) ? stats : undefined;
    },

    getMetricsGroupByViewId: function(metrics, config) {
      var metricsMap = {};
      var metricsGroup = [];

      metrics.forEach(function(metric) {
        if(!metricsMap[metric.viewId]) {
          metricsMap[metric.viewId] = new Array(metric);
        } else {
          metricsMap[metric.viewId].push(metric);
        }
      });

      for (var viewId in metricsMap) {
        var stats = OperationsUtils.getStats(metricsMap[viewId], config.infraCost);

        if(stats) {
          metricsGroup.push({
            stats: stats,
            viewId: viewId,
            responseTimeAlertingLevelWarning: config.responseTimeAlertingLevelWarning,
            responseTimeAlertingLevelError: config.responseTimeAlertingLevelError,
            errorsRateAlertingLevelWarning: config.errorsRateAlertingLevelWarning,
            errorsRateAlertingLevelError: config.errorsRateAlertingLevelError,
            availabilityRateAlertingLevelWarning: config.availabilityRateAlertingLevelWarning,
            availabilityRateAlertingLevelError: config.availabilityRateAlertingLevelError
          });
        }
      }

      return metricsGroup
        .sort(function(m1, m2) {
          var id1 = m1.viewId.toUpperCase(); // ignore upper and lowercase
          var id2 = m2.viewId.toUpperCase(); // ignore upper and lowercase
          if (id1 < id2) {
            return -1;
          }
          if (id1 > id2) {
            return 1;
          }
          return 0;
        })
        .map(function(metric) {
          return JSON.stringify(metric);
        });
    },

    sendEvent: function(emitter, metricName, description, color){
      var event =  {
        detail: {
          emitter: emitter,
          title: metricName,
          dashboard: Utils.getDashboardId(),
          description: emitter + ' - ' + description,
          date: new Date(),
          url: '',
          color: color
        }
      };
      if(!eventMap[emitter] || eventMap[emitter].title !== event.detail.title || eventMap[emitter].description !== event.detail.description || eventMap[emitter].color !== event.detail.color) {
        eventMap[emitter] = {
          emitter: event.detail.title,
          title: event.detail.title,
          description: event.detail.description,
          color: event.detail.color
        };
        document.dispatchEvent(new CustomEvent('HeadsUp', event));
      }
    }

  };

})();
