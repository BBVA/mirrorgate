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

genericTileComponentTest('program-increment-tile','path');

describe('<program-increment-tile>', () => {

  let server;

  beforeEach(() => {
    server = buildFakeServer();
  });

  afterEach(() => {
    server.restore();
  });

  it('should give each product a 1 total size', (done) => {
    createTestComponent('program-increment-tile').then(function (component) {
      server.respond();

      setTimeout(function () {
        let total = 0;
        let pi = component.getModel().programIncrement;

        pi.products.forEach(function(product) {
          let productTotal = 0;
          product.children.forEach(function(feature) {
            if(feature.children && feature.children.length) {
              feature.children.forEach(function(issue) {
                productTotal += issue.size;
              });
            } else {
              productTotal += feature.size;
            }
          });
          expect(productTotal).toBeCloseTo(1,5);
          total += productTotal;
        });

        expect(total).toBeCloseTo(pi.products.length,5);
        done();
      });

    });
  });
});