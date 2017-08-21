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

var ServerSideEvent = function(callback){

    var serverSideEvent;

    function init(){
      if(!serverSideEvent || serverSideEvent.readyState == serverSideEvent.CLOSED){

        serverSideEvent = new EventSource(window.location.protocol +"//"+ window.location.host + "/mirrorgate//emitter/" + Utils.getDashboardId());

        serverSideEvent.onmessage = function(data){
          var response = data.data;
          callback(response);
        };

        serverSideEvent.onclose = function(data){
          console.log("closing connection");
        };

        serverSideEvent.onerror = function(err) {
          console.error('EventSource encountered error: ', err.message, 'Closing EventSource');
          //serverSideEvent.close();
        };
      }
    }

    init();

    Timer.eventually.attach(init);

  };
