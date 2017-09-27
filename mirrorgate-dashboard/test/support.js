
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

function createTestComponent(type) {
  return new Promise((resolve) => {
    var component = document.createElement(type);
    component.addEventListener('component-ready', function () {
      setTimeout(function () {
        resolve(component);
      });
    });
    document.body.appendChild(component);
    component.setAttribute('config', JSON.stringify(detailsForTesting));
  });
}

function fgenericTileComponentTest(type, contentProveSelector) {
  genericTileComponentTest(type, contentProveSelector, true);
}

function genericTileComponentTest(type, contentProveSelector, focus) {

  (focus ? fdescribe : describe)('Generic features for ' + type, () => {
    var server;

    beforeEach(() => {
      server = buildFakeServer();
    });

    it('should show content', (done) => {
      createTestComponent(type).then((component) => {
        server.respond();
        setTimeout(function () {
          let items = component.shadowRoot.querySelectorAll(contentProveSelector);
          expect(items.length).not.toBe(0);
          done();
        });
      });
    });

    describe('events', () => {

      var testHandler;
      var handler = function (e) {
        if (testHandler) {
          testHandler(e);
        }
      };

      beforeEach(() => {
        document.addEventListener('dashboard-updated', handler);
      });

      afterEach(() => {
        document.removeEventListener('dashboard-updated', handler);
        testHandler = undefined;
      });

      it('should raise an event with ok or unknown when it succeds', (done) => {
        testHandler = function (e) {
          expect(e.detail.status).not.toBe('server-error');
          done();
        };

        createTestComponent(type).then(function () {
          server.respond();
        });
      });

      it('should raise an event when servers fail', (done) => {
        server.restore();
        server = sinon.fakeServer.create();
        server.autoRespond = false;

        createTestComponent(type).then(function () {
          server.lastRequest.respond(404, {});
        });

        testHandler = function (e) {
          expect(e.detail.status).toBe('server-error');
          done();
        };

      });

    });

    afterEach(() => {
      server.restore();
    });

  });

}
