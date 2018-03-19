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
 * AlertsController - Controller to handle alerts
 *
 */
var AlertsController = (function(dashboardId) {

  var observable = new Event('AlertsController');
  var config;
  var lastAlerts;

  function flattenArray(array){

    var newArray = [];
    array.forEach((element) => {
      if(element.children){
        newArray = [].concat.apply(newArray, element.children);
      } else {
        newArray.push(element);
      }
    });

    return newArray;
  }

  function sendEvent(alerts){
    var sendNotification = false;
    var notificationColor;
    var notificationTitles = 'You have the following alerts:';

    flatArray = flattenArray(alerts);

    if(lastAlerts !== undefined){
      flatArray.forEach((alert) => {
        if(alert.state == 'red' || alert.state == 'yellow'){
          lastAlerts.forEach((lastAlert) => {
            if((alert.title == lastAlert.title) && (alert.state !=  lastAlert.state)){
              sendNotification = true;
              notificationTitles += '\n' + alert.title;
              if(alert.state == 'red' && notificationColor != 'red'){
                notificationColor = 'red';
              } else {
                notificationColor = alert.state;
              }
            }
          });
        }
      });
    }

    if(sendNotification){
      var event =  {
        detail: {
          title: "Alerts",
          dashboard: Utils.getDashboardId(),
          description: notificationTitles,
          date: new Date(),
          url: '',
          color: notificationColor
        }
      };
      document.dispatchEvent(new CustomEvent('Message', event));
    }
    lastAlerts = flatArray;
  }


  function getAlerts() {

    var headers = config.urlAlertsAuthorization && config.urlAlertsAuthorization.split('&').map(function (item) {
      var pos = item.indexOf('=');
      return pos > 0 ? {
        name: item.substring(0, pos),
        value: item.substring(pos+1, item.length)
      } : {
        name: 'Authorization',
        value: item
      };
    });

    httpGetAsync(config.urlAlerts, function(err, data) {

      function buildAlerts(data) {

        var state;
        if(data.state) {
          state = data.state.currentState || data.state;
        }

        var alert = new Alerts(
          data.title,
          state,
          data.image
        );

        for (var j in data.alerts) {
          var children = buildAlerts(data.alerts[j]);
          alert.addChild(children);
        }

        return alert;
      }

      if(!data) {
        return observable.notify(undefined);
      }
      data = JSON.parse(data);

      var model = {};
      var alert_groups = [];
      var totalAlertsCount = 0;
      var failedAlertsCount = 0;
      var unstableAlertsCount = 0;

      //TODO: to improve
      if(data.alerts) {
        if(data.alerts[0].alerts) { //It is a group

          for (var i in data.alerts) {
            alert_groups.push(buildAlerts(data.alerts[i]));

            for(var j in data.alerts[i].alerts){
              totalAlertsCount += 1;

              if(data.alerts[i].alerts[j].state.currentState === 'red'){
                failedAlertsCount += 1;
              } else if(data.alerts[i].alerts[j].state.currentState === 'black'){
                unstableAlertsCount += 1;
              }
            }
          }
        } else {
          alert_groups.push(buildAlerts(data));
        }
      }

      model.alert_groups = alert_groups;
      model.totalAlertsCount = totalAlertsCount;
      model.failedAlertsCount = failedAlertsCount;
      model.unstableAlertsCount = unstableAlertsCount;

      sendEvent(alert_groups);

      observable.notify(model);
    },{
      headers: headers
    });
  }


  this.observable = observable;
  this.dispose = function() {
    Timer.eventually.detach(getAlerts);
  };
  this.init = function(_config) {
    config = _config;
    if(config.urlAlerts) {
      Timer.eventually.attach(getAlerts);
      getAlerts();
    } else {
      return Promise.reject();
    }
  };

});
