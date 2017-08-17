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

var MirrorGateWebSocket = function(callback){

  var mirrorGateWebSocket;

  function init(){
    if(!mirrorGateWebSocket || mirrorGateWebSocket.readyState == mirrorGateWebSocket.CLOSED){

      mirrorGateWebSocket = new SockJS(window.location.protocol+ "//" + window.location.host + "/mirrorgate/websocket?" + Utils.getDashboardId());

      mirrorGateWebSocket.onmessage = function(data){
        callback(data.data);
      };

      mirrorGateWebSocket.onclose = function(data){
        console.log("closing connection");
      };

      mirrorGateWebSocket.onerror = function(err) {
        console.error('Socket encountered error: ', err.message, 'Closing socket');
        mirrorGateWebSocket.close();
      };
    }
  }

  init();

  Timer.eventually.attach(init);

};

