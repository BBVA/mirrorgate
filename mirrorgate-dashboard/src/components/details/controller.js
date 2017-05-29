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
 * DetailsController - Controller to get team name and logo
 *
 */
var DetailsController = (function(dashboardId) {

  var observable = new Event('TeamController');
  var service = Service.get(Service.types.dashboard, dashboardId);

  function getDetails(response) {
    var details;

    if (response) {
      response = JSON.parse(response);
      details = new Detail(response);
    }

    observable.notify(details);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getDetails);
  };
  this.init = function() { service.addListener(getDetails); };

});
