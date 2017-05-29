
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
 * Clock - Defines a clock to dispach an event in a specific interval
 *
 * @param  {Event} evet Event to be dispach
 * @param  {Event} time Interval time
 */
function Clock(event, time) {
  this._event = event;
  this._time = time;
}

Clock.prototype = {

  start: function() {
    if (this._intervalRef || !this._time) {
      return;
    }
    var self = this;
    this._intervalRef =
        setInterval(function() { self._event.notify(); }, self._time);
  },

  stop: function() {
    if (this._intervalRef) {
      clearInterval(this._intervalRef);
    }
    this._intervalRef = undefined;
  }
};
