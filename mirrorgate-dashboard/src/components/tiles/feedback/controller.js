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
 * FeedbackController - Controller to handle user metric data
 *
 */
var FeedbackController = (function(dashboardId) {
  'user strict';

  var observable = new Event('FeedbackController');

  var metricsService = Service.get(Service.types.userMetrics, dashboardId);
  var marketsService = Service.get(Service.types.apps, dashboardId);

  var _config;

  function getUserMetrics(response) {
    var model;

    if(response) {
      response = JSON.parse(response);
      model = {};
      if(response.length && response.length > 0) {
        model.metrics = {
          rtActiveUsers: 0,
          rtActiveUsersSampleSize: 0,
          rtActiveUsersOneDayValue: 0,
          rtActiveUsersOneDaySampleSize: 0,
          sevenDayUsers: 0,
          sevenDayUsersSampleSize: 0,
          sevenDayUsersOneDayValue: 0,
          sevenDayUsersOneDaySampleSize: 0
        };

        var versions = [];
        var versionsMap = {};
        var metricsMap = {};

        response.forEach(function(metric) {
          var metricId = metric.viewId + metric.name + metric.platform + metric.appVersion;
          metricsMap[metricId] = metricsMap[metricId] && metricsMap[metricId] >= metric.timestamp ? metricsMap[metricId] : metric.timestamp;
          if(metric.name === 'activeUsers' && metric.value && metric.timestamp === metricsMap[metricId]) {
            model.metrics.rtActiveUsers += parseInt(metric.value);
            model.metrics.rtActiveUsersSampleSize += parseInt(metric.sampleSize);
            model.metrics.rtActiveUsersOneDayValue += parseInt(metric.oneDayValue);
            model.metrics.rtActiveUsersOneDaySampleSize += parseInt(metric.oneDaySampleSize);

            if(metric.appVersion && metric.appVersion.match(_config.lastVersion)) {
              var value = parseInt(metric.value);
              var name = Utils.rephraseVersion(metric.appVersion, _config.lastVersion);
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
            }
          } else if(metric.name === '7dayUsers' && metric.value && metric.timestamp === metricsMap[metricId]) {
            model.metrics.sevenDayUsers += parseInt(metric.value);
            model.metrics.sevenDayUsersSampleSize += parseInt(metric.sampleSize);
            model.metrics.sevenDayUsersOneDayValue += parseInt(metric.oneDayValue);
            model.metrics.sevenDayUsersOneDaySampleSize += parseInt(metric.oneDaySampleSize);
          }
        }, this);

        versions = versions.sort(function(a,b) {
          return - Utils.compareVersions(a.cannonical, b.cannonical, _config.lastVersion);
        });

        var activeUsersTendencyChange = Utils.getPercentageDifference(model.metrics.rtActiveUsersOneDayValue / model.metrics.rtActiveUsersOneDaySampleSize || 0, model.metrics.rtActiveUsers / model.metrics.rtActiveUsersSampleSize || 0);
        var activeUsersTendency = activeUsersTendencyChange < -10 ? 'threedown' : activeUsersTendencyChange < -5 ? 'twodown' : activeUsersTendencyChange < -1 ? 'onedown' : activeUsersTendencyChange > 10 ? 'threeup' : activeUsersTendencyChange > 5 ? 'twoup' : activeUsersTendencyChange > 1 ? 'oneup' : 'eq';
        var sevenDayUsersTendencyChange = Utils.getPercentageDifference(model.metrics.sevenDayUsersOneDayValue / model.metrics.sevenDayUsersOneDaySampleSize || 0, model.metrics.sevenDayUsers / model.metrics.sevenDayUsersSampleSize || 0);
        var sevenDayUsersTendency = sevenDayUsersTendencyChange < -10 ? 'threedown' : sevenDayUsersTendencyChange < -5 ? 'twodown' : sevenDayUsersTendencyChange < -1 ? 'onedown' : sevenDayUsersTendencyChange > 10 ? 'threeup' : sevenDayUsersTendencyChange > 5 ? 'twoup' : sevenDayUsersTendencyChange > 1 ? 'oneup' : 'eq';

        model.metrics.lastVersionActiveUsersRate = versions.length > 0 ? parseFloat((100 * versions[0].value / model.metrics.rtActiveUsers).toFixed(2)) : undefined;
        model.metrics.versions = versions.length && versions;
        model.metrics.activeUsersTendency = activeUsersTendency;
        model.metrics.sevenDayUsersTendency = sevenDayUsersTendency;
      }
    }

    setTimeout(function() {
      observable.notify(model);
    });

  }

  function getRates(response) {
    var data;

    if(response) {
      data = {
        apps: []
      };
      var reviews = [];
      var total = {
        ratingShortTerm: 0,
        votesShortTerm: 0,
        ratingLongTerm: 0,
        votesLongTerm: 0,
        ratingTotal: 0,
        votesTotal: 0
      };
      response = JSON.parse(response);
      for(var index in response) {
        var item = response[index];
        total.ratingShortTerm += item.ratingShortTerm;
        total.ratingLongTerm += item.ratingLongTerm;
        total.ratingTotal += item.ratingTotal;
        total.votesShortTerm += item.votesShortTerm;
        total.votesLongTerm += item.votesLongTerm;
        total.votesTotal += item.votesTotal;

        var app = new Market(item);
        reviews = reviews.concat(item.reviews);
        data.apps.push(app);
      }
      data.total = new Market(total);
      data.reviews = reviews.filter((review) =>  {
        return review.timestamp > 0;
      });
      data.reviews = reviews.sort((r1, r2) => {
        return r1.timestamp < r2.timestamp ? 1 : -1;
      }).slice(0,8);
      data.marketsStatsDays = _config.marketsStatsDays;
    }

    setTimeout(function() {
      observable.notify(data);
    });

  }

  this.observable = observable;

  this.dispose = function() {
    this.observable.reset();
    if(_config.analyticViews && _config.analyticViews.length) {
      metricsService.removeListener(getUserMetrics);
    }
    if(_config.applications && _config.applications.length) {
      marketsService.removeListener(getRates);
    }
  };

  this.init = function(config) {
    if(
      !(config.analyticViews && config.analyticViews.length) &&
      !(config.applications && config.applications.length)
    ) {
      return Promise.reject();
    }
    _config = config;
    _config.lastVersion = new RegExp(config.lastVersion);

    if(_config.analyticViews && _config.analyticViews.length) {
      metricsService.addListener(getUserMetrics);
    }
    if(_config.applications && _config.applications.length) {
      marketsService.addListener(getRates);
    }
  };

  this.calculateStars = function (total_rate) {
    var stars = [];
    var rate = Math.round(total_rate * 2) / 2;
    for(i = 0; i < 5; i++ ) {
      if(rate - i >= 1 ) {
        stars.push('star');
      } else if (rate - i > 0 ){
        stars.push('star-half-o');
      } else {
        stars.push('star-o');
      }
    }
    return stars;
  };

});
