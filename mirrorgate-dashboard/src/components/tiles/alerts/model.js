
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
 * Alerts model. This may be both an Alert or a group of Alerts.
 * 
 * @param {String} title  Title's Alert.
 * @param {Object} state  State's Alert.
 * @param {Object} image  Image's Alert.
 */
function Alerts(title, state, image) {
  this.title = title;
  this.state = state;
  this.image = image;
  this.children = [];
}


Alerts.prototype = {

  /**
   * Add a child alerts to current group
   * @param {Alerts} alerts
   */
  addChild: function(alerts) { this.children.push(alerts); }

};
