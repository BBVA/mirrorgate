

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

var Events = {
  _init: function() { this._events = this._events || []; },

  reset: function() {
    this._init();
    this._events.forEach((event) => event.reset());
  },

  register: function(event) {
    this._init();
    this._events.push(event);
  }
};

/**
 * Event - Defines events to implemepent Observer pattern
 *
 * @param  {Object} sender Who sends the event
 */
function Event(sender) {
  this._sender = sender;
  this._listeners = [];
  Events.register(this);
}

Event.prototype = {

  attach: function(listener) {
    this._listeners.push(listener);
    if (this._lastResult) {
      dispatch(listener, this, this._lastResult);
    }
  },

  detach: function(listener) {
    var index = this._listeners.indexOf(listener);
    if (index > -1) {
      this._listeners.splice(index, 1);
    }
  },

  getListeners: function() { return this._listeners; },

  notify: function(args) {
    this._lastResult = args;
    for (var index in this._listeners) {
      dispatch(this._listeners[index], this, this._lastResult);
    }
  },

  getLastResult: function() { return this._lastResult; },

  reset: function() { this._listeners = []; }

};

function dispatch(listener, sender, args) {
  var xargs;
  if (args) {
    xargs = Utils.clone(args);
  }
  listener(xargs, sender);
}
