
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
    document.body.appendChild(component);
    component.setAttribute('config', JSON.stringify(detailsForTesting));
    resolve(component);
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
      server.autoRespond = false;
    });

    it('should show content', (done) => {
      createTestComponent(type).then((component) => {
        component.addEventListener('component-ready', function () {
          let items = component.getRootElement().querySelectorAll(contentProveSelector);
          expect(items.length).not.toBe(0);
          done();
        });
        server.respond();
      });
    });

    describe('events', () => {

      it('should raise an event with ok or unknown when it succeeds', (done) => {
        function handler(e) {
          this.removeEventListener('dashboard-updated', handler);
          expect(e.detail.status).not.toBe('server-error');
          done();
        }

        createTestComponent(type).then(function (component) {
          component.addEventListener('dashboard-updated', handler);
          server.respond();
        });
      });

      it('should raise an event when server fail', (done) => {
        function handler(e) {
          this.removeEventListener('dashboard-updated', handler);
          expect(e.detail.status).toBe('server-error');
          done();
        }

        createTestComponent(type).then(function (component) {
          component.addEventListener('dashboard-updated', handler);
          server.lastRequest.error();
        });

      });

    });

    afterEach(() => {
      server.restore();
    });

  });

}
