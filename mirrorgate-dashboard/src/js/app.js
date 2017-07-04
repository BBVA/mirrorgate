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

'use ustrict';

function MainController() {
  function TileVisibilityHandler(dashboardDetails) {
    var tileConditions = {
      markets: 'applications',
      reviews: 'applications',
      builds: 'codeRepos',
      buildsstats: 'codeRepos',
      'current-sprint': 'boards',
      'next-sprint': 'boards',
      incidences: 'boards',
      'program-increment':'programIncrement',
      bugs: 'boards',
      notifications: 'slackTeam',
      alerts: 'urlAlerts'
    };

    // Display tiles only when details are avail
    if (dashboardDetails) {
      for (var tileClass in tileConditions) {
        var tile = document.querySelector(tileClass + '-tile');
        var detailProperty = dashboardDetails[tileConditions[tileClass]];
        if (tile) {
          tile.setAttribute(
              'enabled',
              (tile && detailProperty && detailProperty.length > 0) === true);
          tile.setAttribute('pid', Utils.getDashboardId());
          tile.setAttribute('pconfig', JSON.stringify(dashboardDetails));
        }
      }
    }
  }

  function dropMenuDraw(response) {
    function compareFunction(dashboardA, dashboardB) {
      var nameA = (dashboardA.displayName || dashboardA.name).toUpperCase();
      var nameB = (dashboardB.displayName || dashboardB.name).toUpperCase();

      return nameA > nameB ? 1 : nameA == nameB ? 0 : -1;
    }
    dashboardsService.removeListener(dropMenuDraw);
    if (response) {
      var dashboards = JSON.parse(response);
      var menu = document.querySelector('.dropdown-menu');
      var html = '';
      for (var i in dashboards.sort(compareFunction)) {
        var dashboard = dashboards[i];
        html += '<li><a href="?board=' + encodeURIComponent(dashboard.name) +
            '">' + (dashboard.displayName || dashboard.name) + '</a></li>';
      }
      menu.innerHTML += html;
    }
  }

  var detailsService =
      Service.get(Service.types.dashboard, Utils.getDashboardId());
  var dashboardsService = Service.get(Service.types.dashboards);

  detailsService.addListener(function(details) {
    details = details && JSON.parse(details);
    TileVisibilityHandler(details);
  });

  dashboardsService.addListener(dropMenuDraw);

  Timer.stop();
  Timer.start();
}


document.addEventListener('DOMContentLoaded', MainController);
window.addEventListener('hashchange', function() {
  TileVisibilityHandler();
  Timer.trigger();
});
window.addEventListener('resize', function() { Timer.trigger(); });
