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
 * AggregateDashboardController - Controller to handle alerts
 *
 */
var AggregateDashboardController = (function(dashboardId) {

  var observable = new Event('AggregateDashboardController');
  this.observable = observable;
  this.dispose = function() {};
  this.init = function(config) {

    var boards = [];

    config.aggregatedDashboards.forEach(function(dashboardId) {
      var data = {
        detail: null,
        id: dashboardId
      };
      boards.push(data);

      Service.get(Service.types.dashboard, dashboardId)
        .addListener(function(details) {
          if(!details) {
            for(index = 0; index < boards.length; index++) {
              if(boards[index].id === dashboardId) {
                boards.splice(index, 1);
                break;
              }
            }
          }
          data.detail = details;
          observable.notify(boards);
        });

    }, this);

    observable.notify(boards);
  };

});
