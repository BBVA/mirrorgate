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

var ServerSentEvent = (function(){

  var event = new Event(this);
  var serverSentEvent;

  function init(){
    if(!serverSentEvent || serverSentEvent.readyState == EventSource.CLOSED){

      serverSentEvent = new EventSource("emitter/" + Utils.getDashboardId());

      serverSentEvent.onmessage = function(data){
        var response = data.data;
        event.notify(response);
      };

      serverSentEvent.onclose = function(data){
        console.debug("closing connection");
      };

      serverSentEvent.addEventListener('error', function(e) {
        if (e.currentTarget.readyState != EventSource.CONNECTING) {
          console.error("EventSource error", e);
        }
      });
    }
  }

  function _checkEventRegistration() {
    if (event.getListeners().length && !this._attached) {
      this._attached = true;
    } else if (!event.getListeners().length && this._attached) {
      this._attached = false;
    }
  }

  init();

  return {
    addListener: function(callback, ignorePrevious) {
      event.attach(callback, ignorePrevious);
      _checkEventRegistration();
    },

    removeListener: function(callback) {
      event.detach(callback);
      _checkEventRegistration();
    }
  };

  })();
