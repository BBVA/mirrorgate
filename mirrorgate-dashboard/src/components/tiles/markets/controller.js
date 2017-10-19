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

/**
 * ApplicationsController - Controller to handle applications
 *
 */
var MarketsController = (function(dashboardId) {

  var observable = new Event('ApplicationsController');
  var service = Service.get(Service.types.apps, dashboardId);

  function getRates(response) {
    var data = {};

    if(response) {
      data.apps = [];
      var reviews = [];
      var total = {
        rating7Days: 0,
        votes7Days: 0,
        ratingMonth: 0,
        votesMonth: 0,
        ratingTotal: 0,
        votesTotal: 0
      };
      response = JSON.parse(response);
      for(var index in response) {
        var item = response[index];
        total.rating7Days += item.rating7Days;
        total.ratingMonth += item.ratingMonth;
        total.ratingTotal += item.ratingTotal;
        total.votes7Days += item.votes7Days;
        total.votesMonth += item.votesMonth;
        total.votesTotal += item.votesTotal;

        var app = new Market(item);
        reviews = reviews.concat(item.reviews);
        data.apps.push(app);
      }
      data.total = new Market(total);
      data.reviews = reviews.filter((review) =>  {
        return review.timestamp > 0;
      });
      data.reviews = reviews.sort((r1, r2) => {
        return r1.timestamp < r2.timestamp ? 1 : -1;
      }).slice(0,8);

    }

    observable.notify(data);
  }

  this.observable = observable;
  this.dispose = function() {
    this.observable.reset();
    service.removeListener(getRates);
  };
  this.init = function(config) {
    if(!config.applications || !config.applications.length) {
      return Promise.reject();
    }
    service.addListener(getRates);
  };

  this.calculateStars = function (total_rate) {
    var stars = [];
    var rate = Math.round(total_rate * 2) / 2;
    for(i = 0; i < 5; i++ ) {
      if(rate - i >= 1 ) {
        stars.push('star');
      } else if (rate - i > 0 ){
        stars.push('star-half-o');
      } else {
        stars.push('star-o');
      }
    }
    return stars;
  };

});
