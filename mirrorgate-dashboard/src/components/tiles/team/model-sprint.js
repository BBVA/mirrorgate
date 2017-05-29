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

var Sprint = (function() {

  'use strict';

  /**
   * Sprint - Depicts Sprint model
   *
   * @param  {String} name Name of the sprint
   * @param  {Date} startDate Start date  of the sprint
   * @param  {Date} endDate End date of the sprint
   */
  function Sprint(name, startDate, endDate) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.stories = [];

    this.sprintDays = Utils.getWorkingDays(this.startDate, this.endDate);
    this.totalStoryPoints = 0;
    this.doneStoryPoints = 0;
    this.countedStoryPoints = 0;
  }

  Sprint.prototype = {

    /**
     * Add a Story to this Sprint plan
     * @param {Story} story Story to be added
     */
    addStory: function(story) {
      this.totalStoryPoints += story.points;
      if (story.status === 'Done') {
        this.doneStoryPoints += story.points;
        this.countedStoryPoints += story.points;
      } else if (story.status === 'In Progress') {
        this.countedStoryPoints += story.points / 2;
      }
      this.pointsPerDay = this.totalStoryPoints / this.sprintDays;
      this.stories.push(story);
    },

    /**
   * Get ratio of done story points
   * @return {Number}  Ratio result
   */
    getDoneRatio: function() {
      return Math.round(this.countedStoryPoints * 100 / this.totalStoryPoints);
    },

    /**
   * Get the advance status of the Sprint
   * @return {String}  Advance Sprint Status
   */
    getAdvanceSprintStatus: function() {
      var beforeSprintDay =
          Utils.getWorkingDays(this.startDate, new Date()) - 1;
      var expectedCurrentPoints = this.pointsPerDay * beforeSprintDay;

      if (this.countedStoryPoints >= expectedCurrentPoints * 0.9) {
        return 'Excellent';
      }

      if (this.countedStoryPoints < expectedCurrentPoints * 0.7) {
        return 'Bad';
      }

      return 'Good';
    },

    getDaysLeft: function() {
      var today = moment(new Date());

      if (today.get('hours') > 12) {
        today.add(1, 'days');
      }
      today.set('hours', 0);

      return Utils.getWorkingDays(today, this.endDate);
    },

    getTotalDays: function() {
      var endDate = moment(this.endDate);
      return endDate.isBefore(this.startDate) ?
          0 :
          endDate.diff(this.startDate, 'days');
    }
  };

  return Sprint;


})();
