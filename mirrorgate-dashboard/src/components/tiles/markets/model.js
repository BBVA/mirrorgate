
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
function Market(name, rate, platform, reviews) {
  this.name = name;
  this.rate = rate;
  this.platform = platform;
  this.reviews = reviews;
  reviews.forEach(function(review) {
    review.icon = {
      android: 'android',
      ios: 'apple'
    }[platform.toLowerCase()];
  });
}
