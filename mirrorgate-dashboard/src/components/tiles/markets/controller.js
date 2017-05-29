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
 * ApplicationsController - Controller to handle applications
 *
 */
var MarketsController = (function(dashboardId) {

  var observable = new Event('ApplicationsController');
  var service = Service.get(Service.types.apps, dashboardId);

  function getRates(response) {
    var apps;

    if(response) {
      apps = [];
      response = JSON.parse(response);
      for(var index in response) {
          var app = new Market(
            response[index].appname,
            response[index].rate,
            response[index].platform,
            response[index].last_review_author,
            response[index].last_review_rate,
            response[index].last_review_timestamp,
            response[index].last_review_comment);
          apps.push(app);
      }
    }

    observable.notify(apps);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getRates);
  };
  this.init = function() { service.addListener(getRates); };

});
