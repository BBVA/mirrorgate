
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

 //Intercepts Karma loading to ensure webcomponents are ready when loaded through polyfill
var loaded = window.__karma__.loaded.bind(window.__karma__);
window.__karma__.loaded = (() => {
  if(window.WebComponents && !window.WebComponents.ready) {
    window.addEventListener('WebComponentsReady', () => {
      loaded();
    });
  } else {
    loaded();
  }
}).bind(window.__karma__);


beforeEach(() => {
  Events.reset();
  Service.reset();
});

window.supportsShadowDOM = document.head.createShadowRoot;

var dashboardForTesting = 'all';

var buildsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/builds');

var buildsStatsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/builds-stats');

var detailsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/details');

var storiesForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/stories');

var appsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/applications');

var bugsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/bugs');

var lastNotification = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/notifications');

var alertsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/alerts');

var piForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/programincrement');

var userMetricsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/user-metrics');

var scmMetricsForTesting = readJSON('test/mocks/dashboards/' + dashboardForTesting + '/scm-metrics');

userMetricsForTesting.forEach( function(metric) {
  metric.timestamp = Date.now();
});

//Placeholder to avoid Server Sent Events error
var EventSource = function (){
  this.addEventListener = function () {};
};

beforeEach(function () {
  Timer.stop();
  Service.reattempt = false;
});

afterEach(function () {
  Service.reset();
});

function buildFakeServer() {

  var server = sinon.fakeServer.create();

  //This is needed in order to not interrupt html downloads for HTML Imports of the polyfill
  XMLHttpRequest.useFilters = true;
  XMLHttpRequest.addFilter((method, url) => {
    return !!url.match(/^.*\.html$/);
  });

  server.autoRespond = false;

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
    'dashboards/' + dashboardForTesting + '/builds/rate',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(buildsStatsForTesting)
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

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/alerts',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(alertsForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/programincrement',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(piForTesting)
    ]
  );

  server.respondWith(
    'GET',
    'dashboards/' + dashboardForTesting + '/user-metrics',
    [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify(userMetricsForTesting)
    ]
  );

  server.respondWith(
      'GET',
      'dashboards/' + dashboardForTesting + '/scm-metrics',
      [
        200,
        { "Content-Type": "application/json" },
        JSON.stringify(scmMetricsForTesting)
      ]
    );

  return server;

}
