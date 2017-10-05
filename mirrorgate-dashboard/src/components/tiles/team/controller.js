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
 * TeamController - Controller to tackle team data
 *
 */
var TeamController = (function(dashboardId) {

  var observable = new Event('TeamController');
  var service = Service.get(Service.types.stories, dashboardId);

  function getActiveUserStories(response) {
    var sprintData;
    if (response) {
      response = JSON.parse(response);
      sprintData = {stats: response.stats};

      var currentSprint = response.currentSprint;

      if (currentSprint && currentSprint.length > 0) {
        var name = currentSprint[0].sSprintName;
        var startDate = currentSprint[0].sprintBeginDate ;
        var endDate = currentSprint[0].sprintEndDate;
        sprintData.currentSprint = new Sprint(name, startDate, endDate);

        for (var index in currentSprint) {
          var story = new Story(
              currentSprint[index].sName,
              currentSprint[index].dEstimate || 0,
              currentSprint[index].sStatus,
              currentSprint[index].url);
          sprintData.currentSprint.addStory(story);
        }
      } else {
        sprintData.currentSprint = new Sprint();
      }
    }
    observable.notify(sprintData);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getActiveUserStories);
  };
  this.init = function(config) {
    if(!config.boards || !config.boards.length) {
      return Promise.reject();
    }
    service.addListener(getActiveUserStories);
  };

});
