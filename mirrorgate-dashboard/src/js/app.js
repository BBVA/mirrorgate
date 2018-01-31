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

(function () {
  document.addEventListener('DOMContentLoaded', function () {
    Timer.stop();
    Timer.start();
  });
  window.addEventListener('resize', function() { Timer.trigger(); });
  ServerSentEvent.addListener(function(serverEventType){
    var serverSentEventtype=JSON.parse(serverEventType);
    if(serverSentEventtype.type === 'DashboardType'){
      window.document.location.reload();
    }
  });

  if (!("Notification" in window)) {
    console.warn("This browser does not support desktop notification");
  }  else if (Notification.permission !== "denied") {
    Notification.requestPermission();
  }

})();
