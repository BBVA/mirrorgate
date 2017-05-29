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

describe('BuildsController', () => {

  var server;
  var controller;
  beforeEach(() => {
    server = buildFakeServer();
    server.autoRespond = true;
    controller = new BuildsController(dashboardForTesting);
    controller.init({});
  });

  afterEach(() => {
    server.restore();
    controller.dispose();
  });

  it('should get last builds properly', (done) => {
    controller.observable.attach((response) => {
      expect(response.buildRoot.children.length).toBe(4);
      done();
    });
  });

  it('should provide the last build timestamp', (done) => {
    controller.observable.attach((response) => {
      expect(response.stats.lastBuildTimestamp).toBe(1491584773370);
      done();
    });
  });

  it('should get builds hierarchically structured by branches', (done) => {
    controller.observable.attach((response) => {
      expect(response.buildRoot).not.toBeNull();
      expect(response.buildRoot.children[1].children[0].status).toBe('Success');
      done();
    });

  });

});
