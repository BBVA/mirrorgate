
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

beforeEach(() => {
  Events.reset();
});

var dashboardForTesting = 'all';

var buildsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/builds');

var detailsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/details');

var storiesForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/stories');

var appsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/applications');

var bugsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/bugs');

var lastNotification = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/notifications');

function buildFakeServer() {

  var server = sinon.fakeServer.create();
  //server.autoRespond = true;

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/builds',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(buildsForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/details',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(detailsForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting  + '/stories',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(storiesForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/applications',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(appsForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/bugs',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(bugsForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/notifications',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(lastNotification)
    ]
  );

  return server;

}
