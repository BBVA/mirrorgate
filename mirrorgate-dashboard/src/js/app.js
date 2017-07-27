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
      alerts: 'urlAlerts',
      'user-metrics': 'analyticViews',
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

      var adaptableBody = document.querySelector('.dashboard.adaptable');
      if(adaptableBody) {
        if(dashboardDetails.slackTeam) {
          adaptableBody.classList.add('with-footer');
        } else {
          adaptableBody.classList.remove('with-footer');
        }
      }
    }
  }

  function getRecent() {
    var recent = localStorage.recentDashboards ? JSON.parse(localStorage.recentDashboards) : [];
    var currentPos = recent.indexOf(Utils.getDashboardId());
    if (currentPos >= 0) {
      recent.splice(currentPos,1);
    }
    recent.unshift(Utils.getDashboardId());
    if (recent.length > 8) {
      recent.pop();
    }
    localStorage.recentDashboards = JSON.stringify(recent);

    return recent;

  }

  function dropMenuDraw(response) {
    var i, dashboard;
    dashboardsService.removeListener(dropMenuDraw);
    if (response) {
      var dashboards = JSON.parse(response);
      var menu = document.querySelector('.dropdown-menu');
      var recent = getRecent();

      var html = '';
      var dashboardMap = {};
      for (i in dashboards) {
        dashboard = dashboards[i];
        dashboardMap[dashboard.name] = dashboard;
      }
      for (i of recent) {
        dashboard = dashboardMap[i];
        if(dashboard) {
          html += '<li><a href="?board=' + encodeURIComponent(dashboard.name) +
              '">' + (dashboard.displayName || dashboard.name) + '</a></li>';
        }
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
