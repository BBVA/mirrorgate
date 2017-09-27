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

describe('BugsController', () => {

  var server;
  var controller;

  beforeEach(() => {
    server = buildFakeServer();
    server.autoRespond = true;
    controller = new BugsController(dashboardForTesting);
    return controller.init(detailsForTesting);
  });

  it('should get bugs type count', (done) => {
    var bugs = { minor: 1, major: 0, critical: 0, medium: 1, total: 2 };

    controller.observable.attach((response) => {
      expect(response).toEqual(bugs);
      done();
    });
  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });

});
