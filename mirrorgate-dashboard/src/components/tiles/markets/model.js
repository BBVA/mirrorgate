
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
 * Application model
 *
 * @param {String} name     Application's name
 * @param {Number} rate     Review rates average
 * @param {String} platform Platform definition (Android, IOS...)
 * @param {String} last_review_author Last review author of the app
 * @param {Number} last_review_rate Last review rate of the app
 * @param {Number} last_review_timestamp Last review rate of the app
 * @param {String} last_review_comment Last review comment of the app
 */
function Market(data) {
  this.name = data.appname;
  this.rateTotal = Math.round(data.ratingTotal/data.votesTotal * 10) / 10;
  this.votesTotal = data.votesTotal;
  this.rate7Days = Math.round(data.rating7Days/data.votes7Days * 10) / 10;
  this.votes7Days = data.votes7Days;
  this.rateMonth = Math.round(data.ratingMonth/data.votesMonth * 10) / 10;
  this.votesMonth = data.votesMonth;

  this.tendencyChange = (this.rate7Days - this.rateMonth) / this.rateMonth * 100;
  this.voteTendencyChange = (this.votes7Days * 30 / 7 - this.votesMonth) / this.votesMonth * 100;
  this.tendency = this.tendencyChange < -10 ? 'threedown' : this.tendencyChange < -5 ? 'twodown' : this.tendencyChange < -1 ? 'onedown' : this.tendencyChange > 10 ? 'threeup' : this.tendencyChange > 5 ? 'twoup' : this.tendencyChange > 1 ? 'oneup' : 'eq';
  this.voteTendency = this.voteTendencyChange < -5 ? 'down' : this.voteTendencyChange > 5 ? 'up' : 'eq';
  this.platform = data.platform;
  this.reviews = data.reviews;
  if(this.reviews) {
    this.reviews.forEach((review) => {
      review.commentMood = review.rate <= 1 ? 'sad' : review.rate >= 4 ? 'happy' : 'normal';
    });
  }

  this.icon = this.platform && {
      android: 'android',
      ios: 'apple'
  }[this.platform.toLowerCase()];

  if(this.reviews) {
    this.reviews.forEach(function(review) {
      review.icon = this.icon;
    }.bind(this));
  }

}
