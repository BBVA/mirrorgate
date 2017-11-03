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

describe('Utils',  function() {
  describe('Working Days',  function()  {

    it('should calculate working days diff properly', function() {
      // Normal
      expect(Utils.getWorkingDays(new Date(2017, 2, 1), new Date(2017, 2, 15)))
          .toBe(10);

      // Normal
      expect(Utils.getWorkingDays(new Date(2017, 2, 21), new Date(2017, 3, 4)))
          .toBe(10);

      // Normal
      expect(Utils.getWorkingDays(new Date(2017, 2, 24), new Date(2017, 3, 4)))
          .toBe(7);

      // Inverse order
      expect(Utils.getWorkingDays(new Date(2017, 2, 1), new Date(2017, 1, 15)))
          .toBe(0);

      // Same day
      expect(Utils.getWorkingDays(new Date(2017, 2, 1), new Date(2017, 2, 1)))
          .toBe(0);

      // Inside same week
      expect(Utils.getWorkingDays(new Date(2017, 2, 1), new Date(2017, 2, 2)))
          .toBe(1);

      // From saturday to sunday
      expect(Utils.getWorkingDays(new Date(2017, 2, 25), new Date(2017, 2, 26)))
          .toBe(0);

      // From day to sunday
      expect(Utils.getWorkingDays(new Date(2017, 2, 23), new Date(2017, 2, 26)))
          .toBe(2);

      // From day to sunday
      expect(Utils.getWorkingDays(new Date(2017, 2, 26), new Date(2017, 2, 25)))
          .toBe(0);

    });
  });

  describe('normalEstimation', function () {
    it('should correctly calculate the average', function () {
      expect(Utils.normalEstimation([1]).avg).toBe(1);
      expect(Utils.normalEstimation([1,2,3]).avg).toBe(2);
      expect(Utils.normalEstimation([]).avg).toBeNaN();
      expect(Utils.normalEstimation().avg).toBeNaN();
    });

    it('should correctly calculate the estandar deviation', function () {
      expect(Utils.normalEstimation([1,2,3]).sigma).toBeCloseTo(0.632,3);
      expect(Utils.normalEstimation([1]).sigma).toBeNaN();
      expect(Utils.normalEstimation([]).avg).toBeNaN();
      expect(Utils.normalEstimation().avg).toBeNaN();
    });

    it('should correctly perform estimate', function () {
      expect(Utils.normalEstimation([1,2,3]).estimate).toBeCloseTo(2.531,3);
      expect(Utils.normalEstimation([1]).estimate).toBeNaN();
      expect(Utils.normalEstimation([]).estimate).toBeNaN();
      expect(Utils.normalEstimation().estimate).toBeNaN();
    });
  });

  describe('rephraseVersions', function () {
    it('should rephrase versions according to regexp', function() {
      var regExp = new RegExp('^(\\d{1,2})\\.(\\d+).*$');

      expect(Utils.rephraseVersion('1.2.3', regExp)).toBe('v1.2');
      expect(Utils.rephraseVersion('1.2', regExp)).toBe('v1.2');
      expect(Utils.rephraseVersion('1.3.5', regExp)).toBe('v1.3');
    });

    it('should rephrase versions falling back to version parameter', function() {
      var regExp = new RegExp('^(\\d{1,2})\\.(\\d+).*$');

      expect(Utils.rephraseVersion('1', regExp)).toBe('1');
      expect(Utils.rephraseVersion('pepe', regExp)).toBe('pepe');
    });
  });

  describe('compareVersions', function() {

    it('should compare application version properly', function() {
        var regExp = new RegExp('^(\\d{1,2})\\.(\\d+)\\.?(\\d+)?$');

        // v1 > v2 then greater than 0
        expect(Utils.compareVersions('6.3.3', '5.4.2', regExp)).toBeGreaterThan(0);

        // v2 > v1 then less than 0
        expect(Utils.compareVersions('6.3.3', '7.4.3', regExp)).toBeLessThan(0);

        // v2 = v1 then 0
        expect(Utils.compareVersions('6.3.3', '6.3.3', regExp)).toBe(0);

        // v1 does not match regExp then less than 0
        expect(Utils.compareVersions('atreyu', '6.3.3', regExp)).toBeLessThan(0);

        // v2 does not match regExp then greater than 0
        expect(Utils.compareVersions('6.3.3', 'atreyu', regExp)).toBeGreaterThan(0);

        // v1 and v2 does not match regExp then 0
        expect(Utils.compareVersions('atreyu', 'mirrorgate', regExp)).toBe(0);

        // v1 > v2 and v1.length > v2.length then greater than 0
        expect(Utils.compareVersions('6.3.3', '5.4', regExp)).toBeGreaterThan(0);

        // v1 > v2 and v2.length > v1.length then greater than 0
        expect(Utils.compareVersions('6.3', '5.4.6', regExp)).toBeGreaterThan(0);

        // v1 > v2 and v1 out of range then less than 0
        expect(Utils.compareVersions('9999.3.0', '5.4.6', regExp)).toBeLessThan(0);

        // v1 > v2 and v2 out of range then greater than 0
        expect(Utils.compareVersions('6.3', '9999.3.0', regExp)).toBeGreaterThan(0);
    });

  });

});
