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

describe('MarketsController', () => {

  var baseMarketData = {
    votesTotal: 10,
    ratingTotal: 36,
    votes7Days: 2,
    rating7Days: 5,
    votesMonth: 3,
    ratingMonth: 10,
    reviews: [
      {
        author: 'kike',
        rate: 1.0,
        timestamp: 1498804350000,
        comment: 'Consume mucho'
      },
      {
        author: 'Ana',
        rate: 3.5,
        timestamp: 1488961673755,
        comment: 'Buen diseño'
      },
      {
        author: 'Alfonso',
        rate: 5,
        timestamp: 1498814350000,
        comment: 'Me gusta'
      }
    ]
  };

  describe('comment mood calculation', () => {
    it('should set the rate type according to rating', () => {
      let market = new Market(Object.assign({}, baseMarketData));

      expect(market.reviews[0].commentMood).toEqual('sad');
      expect(market.reviews[1].commentMood).toEqual('normal');
      expect(market.reviews[2].commentMood).toEqual('happy');
    });
  });

  describe('rate calculation', () => {
    it('calculates rates correctly when empty data', () => {
      let market = new Market({});
      expect(market.rateTotal).toEqual(NaN);
      expect(market.rate7Days).toEqual(NaN);
      expect(market.rateMonth).toEqual(NaN);
    });

    it('calculates rates correctly and rounds them', () => {
      let market = new Market(Object.assign({}, baseMarketData));
      expect(market.rateTotal).toEqual(3.6);
      expect(market.rate7Days).toEqual(2.5);
      expect(market.rateMonth).toEqual(3.3);
    });

    it('calculates rates tendencies correctly', () => {
      let market = new Market(Object.assign({}, baseMarketData));
      expect(market.tendency).toEqual('threedown');
      expect(Math.round(market.tendencyChange)).toEqual(-24);

      market = new Market(Object.assign({},baseMarketData, {votesMonth: 9}));
      expect(Math.round(market.tendencyChange)).toEqual(127);

    });

  });

});
