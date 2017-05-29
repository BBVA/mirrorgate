
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

describe('Market', () => {

  var market;
  
  var name = 'mirrorgate';
  var rate = 4.5;
  var platform = 'Android';
  var last_review_author = 'Atreyu';
  var last_review_rate = 5;
  var last_review_timestamp = 1;
  var last_review_comment = 'Awesome!!!';
  
  it('shoud define default parameters', () => {
    market = new market();
    expect(market.name).toBe(undefined);
    expect(market.rate).toBe(undefined);
    expect(market.platform).toBe(undefined);
    expect(market.last_review_author).toBe(undefined);
    expect(market.last_review_rate).toBe(undefined);
    expect(market.last_review_timestamp).toBe(undefined);
    expect(market.last_review_comment).toBe(undefined);
  });

  it('should allow custome parameters', () => {
    market = new Market(name, rate, platform, last_review_author);
    expect(market.name).toBe(name);
    expect(market.rate).toBe(rate);
    expect(market.platform).toBe(platform);
    expect(market.last_review_author).toBe(last_review_author);
    expect(market.last_review_rate).toBe(last_review_rate);
    expect(market.last_review_timestamp).toBe(last_review_timestamp);
    expect(market.last_review_comment).toBe(last_review_comment);
  });

});
