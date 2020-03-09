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

describe('TeamController', () => {

  var server;
  var controller;
  beforeEach(() => {
    server = buildFakeServer();
    server.autoRespond = true;
    controller = new TeamController(dashboardForTesting);
    return controller.init(detailsForTesting);
  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });

  it('get current sprint stories properly', (done) => {
    var stories = storiesForTesting.currentSprint;

    var name = stories[0].sprintName;
    var startDate = stories[0].sprintBeginDate ?
        stories[0].sprintBeginDate :
        new Date();
    var endDate = stories[0].sprintEndDate ?
        stories[0].sprintEndDate :
        new Date();
    var sprint = new Sprint(name, startDate, endDate);

    for (var index in stories) {
      var story = new Story(
          stories[index].name, stories[index].estimation,
          stories[index].status);
      sprint.addStory(story);
    }

    controller.observable.attach((response) => {
      expect(response.currentSprint).toEqual(sprint);
      done();
    });
  });

});
