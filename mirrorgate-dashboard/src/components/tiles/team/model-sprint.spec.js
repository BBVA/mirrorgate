
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

describe('Sprint', () => {

  var sprint;
  var name = 'sprint1';
  var doneStory = new Story('doneStory', 5, 'Done');
  var inProgressStory = new Story('inProgressStory', 3, 'In Progress');
  var blockedStory = new Story('blockStory', 5, 'Blocked');
  var startDate, endDate;

  beforeEach(() => {
    jasmine.clock().install();
    var baseTime = new Date(2017, 2, 22);
    jasmine.clock().mockDate(baseTime);
    startDate = new Date();
    startDate.setDate(baseTime.getDate() - 2);
    endDate = baseTime;
  });

  afterEach(() => { jasmine.clock().uninstall(); });

  it('should allow enter some parameters', () => {
    var status = 'Success';
    var size = 50;
    sprint = new Sprint(name, startDate, endDate);
    expect(sprint.name).toBe(name);
    expect(sprint.startDate).toBe(startDate);
    expect(sprint.endDate).toBe(endDate);
  });

  describe('addStory function', () => {

    beforeEach(() => { sprint = new Sprint(name, startDate, endDate); });

    it('should allow to add stories to the sprint', () => {
      sprint.addStory(doneStory);
      sprint.addStory(inProgressStory);
      sprint.addStory(blockedStory);
      expect(_.isEqual(sprint.stories[0], doneStory)).toBe(true);
      expect(_.isEqual(sprint.stories[1], inProgressStory)).toBe(true);
      expect(_.isEqual(sprint.stories[2], blockedStory)).toBe(true);
    });
  });

  describe('getDoneRatio function', () => {

    beforeEach(() => { sprint = new Sprint(name, startDate, endDate); });

    it('should get the rate of done story points properly', () => {
      var totalStoryPoints =
          doneStory.points + inProgressStory.points + blockedStory.points;
      var expectedRate = Math.round(
          (doneStory.points + inProgressStory.points / 2) * 100 /
          totalStoryPoints);

      sprint.addStory(doneStory);
      sprint.addStory(inProgressStory);
      sprint.addStory(blockedStory);
      expect(sprint.getDoneRatio()).toBe(expectedRate);
    });
  });

  describe('getDoneRatio function', () => {

    beforeEach(() => { sprint = new Sprint(name, startDate, endDate); });

    it('should get the rate of done story points properly', () => {
      var totalStoryPoints =
          doneStory.points + inProgressStory.points + blockedStory.points;
      var expectedRate = Math.round(
          (doneStory.points + inProgressStory.points / 2) * 100 /
          totalStoryPoints);

      sprint.addStory(doneStory);
      sprint.addStory(inProgressStory);
      sprint.addStory(blockedStory);
      expect(sprint.getDoneRatio()).toBe(expectedRate);
    });
  });

  describe('getTotalDays', () => {

    it('should calculate the total days inside the sprint', () => {
      var totalDays = new Sprint('', startDate, endDate).getTotalDays();
      expect(totalDays).toBe(2);
    });

    it('should return 0 when startDate and endDate are the same', () => {
      var totalDays = new Sprint('', startDate, startDate).getTotalDays();
      expect(totalDays).toBe(0);
    });

    it('should return 0 when startDate is after endDate', () => {
      var totalDays = new Sprint('', endDate, startDate).getTotalDays();
      expect(totalDays).toBe(0);
    });
  });

  describe('getAdvanceSprintStatus function', () => {

    beforeEach(() => { sprint = new Sprint(name, startDate, endDate); });

    it('should be "Bad" if the rate of advanced sprint is less than of 70% regarding a day before',
       () => {
         var doneStory = new Story('Done', 6, 'Done');
         var blockedStory = new Story('Done', 14, 'Blocked');

         sprint.addStory(doneStory);
         sprint.addStory(blockedStory);
         expect(sprint.getAdvanceSprintStatus()).toBe('Bad');
       });

    it('should be "Good" if the rate of advanced sprint is less than of 90% and grater than 70% regarding a day before',
       () => {
         var doneStory = new Story('Done', 7, 'Done');
         var blockedStory = new Story('Done', 13, 'Blocked');

         sprint.addStory(doneStory);
         sprint.addStory(blockedStory);
         expect(sprint.getAdvanceSprintStatus()).toBe('Good');
       });

    it('should be "Excellent" if the rate of advanced sprint is less than of 90% and grater than 90% regarding a day before',
       () => {
         var doneStory = new Story('Done', 9, 'Done');
         var blockedStory = new Story('Done', 11, 'Blocked');

         sprint.addStory(doneStory);
         sprint.addStory(blockedStory);
         expect(sprint.getAdvanceSprintStatus()).toBe('Excellent');
       });
  });

});
