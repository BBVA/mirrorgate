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

describe('AlertsController', () => {

  var server;
  var controller;

  beforeEach(() => {
    server = buildFakeServer();
    
    server.autoRespond = true;
    controller = new AlertsController(dashboardForTesting);
    controller.init();
  });

  it('should get alerts', (done) => {
    
    alert_groups = [];
    for (var i in alertsForTesting.alerts) {
      alert_groups.push(buildAlerts(alertsForTesting.alerts[i], 6));
      /* Just show 2 groups of alerts */
      if(alert_groups.length == 2) {
        break;
      }
    }

    controller.observable.attach((response) => {
      expect(response).toEqual(alert_groups);
      done();
    });
  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });
  
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
});
