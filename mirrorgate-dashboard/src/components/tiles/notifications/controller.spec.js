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

describe('NotificationsController', () => {

  var server;
  var controller;
  var ws;
  
  beforeEach(() => {
    server = buildFakeServer();
    server.autoRespond = true;
    
    sinon.stub(window, 'WebSocket').returns(ws);

    controller = new NotificationsController(dashboardForTesting);
    controller.init();
  });

  xit('should get active notifications', (done) => {
    var notifications = [];

    for (var index in notificationsForTesting) {
      var notification = new Notification(
        notificationsForTesting[index].title,
        notificationsForTesting[index].descrition,
        notificationsForTesting[index].date);
      notifications.push(notification);
    }
        
    controller.observable.attach((response) => {
      
      console.log('EEE');

      expect(_.isEqual(response, notifications)).toBe(true);
      done();
    });
  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });

});
