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

  beforeEach(() => {
    server = buildFakeServer();
    server.autoRespond = true;

    controller = new NotificationsController(dashboardForTesting);
    return controller.init(detailsForTesting);
  });

  it('should get last notification', (done) => {

    var notification = new Notification(
      lastNotification.text,
      new Date(parseFloat(lastNotification.ts) * 1000) ,
      lastNotification.username,
      lastNotification.attachments[0].color
    );

    controller.observable.attach((response) => {
      expect(_.isEqual(response, notification)).toBe(true);
      done();
    });

  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });

});
