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

  var config;
  var observable = new Event('NotificationsController');
  var service = Service.get(Service.types.notifications, dashboardId);

  function getWebSocketURL(response) {

    function loadNotification(notification) {

      var colorMapping = {
        'daa038' : 'yellow', // Warning status color for Slack
        'd00000': 'red', // Danger status color for Slack
      };

      if('message' === notification.type) {
        if(config.slackChannel && config.slackChannel != notification.channel) {
          return;
        }
        var attachment = (notification.attachments &&
            notification.attachments[0]);

        document.dispatchEvent(new CustomEvent('Message', {
          detail: {
            description: notification.text || (attachment && (attachment.pretext || attachment.fallback)),
            date: new Date(parseFloat(notification.ts) * 1000),
            user: notification.username,
            color: colorMapping[(attachment && attachment.color && attachment.color.toLowerCase())]
          }
        }));
      }
    }

    if(!response) {
        return document.dispatchEvent(new CustomEvent('Message', undefined));
    }

    if(response.indexOf('ws') === 0) {

      var webSocket = new WebSocket(response);

      webSocket.onmessage = function (event) {
        var notification = JSON.parse(event.data);
        loadNotification(notification);
      };
    } else {
      loadNotification(JSON.parse(response));
    }
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getWebSocketURL);
  };
  this.init = function(_config) {
    config = _config;
    if(!config.slackTeam) {
      return Promise.reject();
    }
    service.addListener(getWebSocketURL);
  };

});
