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

describe('Utils', () => {
  describe('Working Days', () => {

    it('should calculate working days diff properly', () => {
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
});
