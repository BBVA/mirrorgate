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
 * NotificationsController - Controller to handle notifications
 *
 */
var NotificationsController = (function(dashboardId) {

  var observable = new Event('NotificationsController');
  var service = Service.get(Service.types.notifications, dashboardId);

  function getWebSocketURL(response) {

    if(!response) {
        return;
    }

    var webSocket = new WebSocket(response);

    webSocket.onmessage = function (event) {

      var slack_notification = JSON.parse(event.data);

      if('message' === slack_notification.type) {
        var attachment = (slack_notification.attachments &&
            slack_notification.attachments[0])

        var notification = new Notification(
          slack_notification.text || (attachment && (attachment.pretext || attachment.fallback)),
          new Date(parseFloat(slack_notification.ts) * 1000) ,
          slack_notification.username,
          (attachment && attachment.color) || 'fff'
        );
        observable.notify(notification);
      }
    };
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getWebSocketURL);
  };
  this.init = function() {
    service.addListener(getWebSocketURL);
  };

});
