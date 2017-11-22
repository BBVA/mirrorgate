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

    function buildProgramIncrementReport (programIncrement) {
      if(!programIncrement.stories || !programIncrement.features) {
        return;
      }

      var spEstimation = Utils.normalEstimation(
          programIncrement.stories.filter(function (story) {
              return story.status !== 'BACKLOG' || story.estimate !== 0;
          }).map(function (story) {
              return story.estimate || 0;
          })
      );

      var ftEstimation = Utils.normalEstimation(
          programIncrement.features.filter(function(feature) {
              return feature.status !== 'BACKLOG';
          }).map(function (feature) {
              return feature.children.length;
          })
      );

      var report = {
          total: 0,
          totalEstimate: 0,
          perStatus: {}
      };

      programIncrement.features.forEach(function (feature) {
        if(feature.status !== 'DONE') {
            let diff = ftEstimation.estimate - feature.children.length;
            //We assume that the degree of uncertainty is less if the feature is at least ready
            if(feature.status !== 'BACKLOG' && feature.children.length > 0) {
              diff = diff / 2;
            }
            if(diff > 0) {
                //report.totalEstimate += diff * spEstimation.estimate;
            }
            feature.children.forEach(function(story) {
                if(story.estimate === 0 && story.status === 'BACKLOG') {
                    report.totalEstimate += spEstimation.estimate;
                }
            }, this);
        }
        feature.children.forEach(function(story) {
            if(story.estimate) {
              report.perStatus[story.status] = (report.perStatus[story.status] || 0) + story.estimate;
              report.total += story.estimate;
              report.totalEstimate += story.estimate;
            }
        }, this);
      });

      report.completed = (Math.round(report.perStatus.DONE/report.totalEstimate * 10000) / 100);
      return (programIncrement.report = report);
    }

    function getProductSetFromFeatures(features) {
      return features.reduce(function(productSet, feat) {
        if (!productSet[feat.project.name]) {
          productSet[feat.project.name] = {
            name: feat.project.name,
            children: [],
            completed: 0,
            count: 0
          };
        }
        return productSet;
      }, {});
    }


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
          var productSet = getProductSetFromFeatures(arg.programIncrementFeatures);

          if(Object.keys(productSet).length === 1) {
            arg.programIncrementEpics.forEach(function(epic) {
              productSet[epic.jiraKey] = {
                name: epic.name,
                url: epic.url,
                children: [],
                completed: 0,
                count: 0
              };
            });
          }

          arg.programIncrementFeatures.forEach(function(feat) {
            featMap[feat.jiraKey] = feat;
            feat.children = [];
            productKey = feat.project.name;
            if(feat.parentKey) {
              for(var i =0; i<feat.parentKey.length; i++) {
                if(productSet[feat.parentKey[i]]) {
                  productKey = feat.parentKey[i];
                  break;
                }
              }
            }
            productSet[productKey].children.push(feat);
            productSet[productKey].completed += feat.status === 'DONE' ? 1 : 0;
            productSet[productKey].count++;
          }, this);
          var productArray = [];
          for (var i in productSet){
             productArray.push(productSet[i]);
          }

          if(arg.programIncrementStories) {
            arg.programIncrementStories.forEach(function(story) {
              feat = featMap[story.parentKey];
              if (feat) { feat.children.push(story); }
            }, this);
          }

          programIncrement = new ProgramIncrement(completed, arg.programIncrementFeatures, arg.programIncrementStories, productArray, arg.programIncrementStartDate, arg.programIncrementEndDate);
        } else {
          programIncrement = {};
        }
        buildProgramIncrementReport(programIncrement);
      }

      observable.notify(programIncrement);

    }

    this.getProgramIncrementStatus = function (programIncrement) {
      if(!programIncrement) return;

      var totalWorkingDays = Utils.getWorkingDays(programIncrement.startDate,programIncrement.endDate);
      var remainingWorkingDays = programIncrement.getDaysLeft();
      var timePassed = (totalWorkingDays - remainingWorkingDays) /totalWorkingDays * 100;

      //If no more than 10% of time has passed, don't take a decission yet
      var diff = timePassed > 10 ? (programIncrement.report.completed - timePassed) : 0;

      return diff < - 20 || isNaN(diff) ? diff < -40 ?
          'error' :
          'warn' :
          'ok';
    };

    this.observable = observable;
    this.dispose = function() {
      this.observable.reset();
      service.removeListener(getProgramIncrement);
    };
    this.init = function(config) {
      if(!config.boards || !config.boards.length || !config.programIncrement) {
        return Promise.reject();
      }
      service.addListener(getProgramIncrement);
    };
  }
);
