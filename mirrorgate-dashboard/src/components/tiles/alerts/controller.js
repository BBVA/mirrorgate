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

    httpGetAsync(config.urlAlerts, function(data) {

      function buildAlerts(data, limit) {

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
          /* Just show limited alerts for group */
          if(limit && j == limit) {
            break;
          }

          var children = buildAlerts(data.alerts[j]);
          alert.addChild(children);
        }

        return alert;
      }

      if(!data) {
        return observable.notify(undefined);
      }
      data = JSON.parse(data);

      var alert_groups = [];

      //TODO: to improve
      if(data.alerts) {
        if(data.alerts[0].alerts) { //It is a group

          for (var i in data.alerts) {
            alert_groups.push(buildAlerts(data.alerts[i], 6));
            /* Just show 2 groups of alerts */
            if(alert_groups.length == 2) {
              break;
            }
          }
        } else {
          alert_groups.push(buildAlerts(data, 12));
        }
      }

      observable.notify(alert_groups);
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
    }
  };

});
