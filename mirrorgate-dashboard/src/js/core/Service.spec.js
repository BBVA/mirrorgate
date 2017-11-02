
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

describe('Service', function() {

  var server;

  beforeEach(function() {
    server = buildFakeServer();
    server.autoRespond = true;
  });

  afterEach(function() {
    server.restore();
  });

  it('should get current sprint stories properly', function(done) {
    var service = Service.get(Service.types.stories, dashboardForTesting);
    service.addListener((response) => {
      expect(response).toBe(JSON.stringify(storiesForTesting));
      done();
    });
  });

  describe ('reattempt system', function() {

    var data = 'fakeData';
    var attempt;

    beforeEach(function() {
      jasmine.clock().install();
      server = sinon.fakeServer.create();
      server.autoRespond = true;
      service = Service.get({
        _instances: {},
        timer: Timer.never,
        serverEventType: 'fakeType',
        getUrl: function () { return 'fakeURl'}
      }, dashboardForTesting);

      service.addListener(function (response) {
        if(response) {
          expect(response).toBe(data);
        }
      });

    });

    it('should not reattempt if service gets success', function () {
      server.respondWith(data);

      jasmine.clock().tick(32000);

      expect(server.requests.length).toBe(1);
    });

    it('should reattempt serveral times in a progresive way if service gets error', function () {
      for (attempt = 1; attempt < 20 ; attempt++) {
        jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      }

      expect(server.requests.length).toBe(attempt);

      server.respondWith(data);

      jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      attempt++;

      expect(server.requests.length).toBe(attempt);
    });

    it('should reattempt serveral times in a progresive way until service gets success', function () {
      for (attempt = 1; attempt < 10 ; attempt++) {
        jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      }

      expect(server.requests.length).toBe(attempt);

      server.respondWith(data);

      jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      attempt++;

      jasmine.clock().tick(50000); // Last response was success so does not reattempt

      expect(server.requests.length).toBe(attempt);
    });

    it('should continue reattempting when Timer is trigger', function () {
      for (attempt = 1; attempt < 10 ; attempt++) {
        jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      }

      expect(server.requests.length).toBe(attempt);

      Timer.never.notify();
      attempt++;

      jasmine.clock().tick(Math.min(32010, Math.pow(2, attempt) * 1000) + 10);
      attempt++;

      expect(server.requests.length).toBe(attempt);
    });

    afterEach(function() {
      service.dispose();
      server.restore();
      jasmine.clock().uninstall();
    });

  });

});
