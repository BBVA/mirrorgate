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
 * ProductIncrementController controller to show product increment information
 */

var ProgramIncrementController = (
  function(dashboardId){
    var observable = new Event('ProgramIncrementController');
    var service = Service.get(Service.types.programincrement, dashboardId);

    function getProgramIncrement(response) {
      var programIncrement;

      if(response) {
        var arg = JSON.parse(response);
        //See: mirrorgate-api/src/main/java/com/bbva/arq/devops/ae/mirrorgate/dto/ProgramIncrementDTO.java
        if(arg.programIncrementStories) {
          var completed = arg.programIncrementFeatures.filter(function(element) {
            return element.status === 'DONE';
          }, this);
          var featMap = {};
          arg.programIncrementFeatures.forEach(function(feat) {
            featMap[feat.jiraKey] = feat;
            feat.children = [];
          }, this);
          if(arg.programIncrementStories) {
            arg.programIncrementStories.forEach(function(story) {
              feat = featMap[story.parentKey];
              if (feat) { feat.children.push(story); }
            }, this);
          }
          programIncrement = new ProgramIncrement(completed, arg.programIncrementFeatures, arg.programIncrementStories);
        } else {
          programIncrement = {};
        }
      }

      observable.notify(programIncrement);

    }

    this.observable = observable;
    this.dispose = function() {
      this.observable.reset();
      service.removeListener(getProgramIncrement);
    };
    this.init = function() {
      service.addListener(getProgramIncrement);
    };
  }
);
