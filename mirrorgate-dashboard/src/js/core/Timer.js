
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
 * Timer - Dispach events with Clocks
 * @Singleton
 */
var Timer = (function() {

  var timerDefinitions = {
    'frequently': 2000,
    'eventually': 30000,
    'rarely': 300000,
    'never': 0,
  };

  var timers = {};

  for (var timer in timerDefinitions) {
    timers[timer] = new Event(timer);
    timers[timer]._clock = new Clock(timers[timer], timerDefinitions[timer]);
    timers[timer]._interval = timerDefinitions[timer];
  }

  timers.trigger = function() {
    timers.stop();
    timers.start();
    for (var timer in timerDefinitions) {
      timers[timer].notify();
    }
  };

  timers.start = function() {
    for (var timer in timerDefinitions) {
      timers[timer]._clock.stop();
      timers[timer]._clock.start();
    }
  };

  timers.stop = function() {
    for (var timer in timerDefinitions) {
      timers[timer]._clock.stop();
    }
  };

  return timers;

})();
