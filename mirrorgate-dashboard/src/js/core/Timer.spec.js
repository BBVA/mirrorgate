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

describe('Timer', () => {

  beforeEach(() => {
    Timer.stop();
    jasmine.clock().install();
    Timer.start();
  });

  afterEach(() => {
    Timer.stop();
    jasmine.clock().uninstall();
  });

  it('should throw events properly', () => {
    var done = false;
    Timer.frequently.attach(() => { done = true; });
    jasmine.clock().tick(Timer.frequently._interval - 1);
    expect(done).toBe(false);
    jasmine.clock().tick(2);
    expect(done).toBe(true);
  });
});
