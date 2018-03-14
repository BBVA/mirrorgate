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

(function() {
  'use strict';

  var service = Service.get(Service.types.eventNotifications, Utils.getDashboardId());

  function showNotification(response){
    if (response){
      var JSONResponse = JSON.parse(response);
      document.dispatchEvent(new CustomEvent('Message', {
        detail: {
          title: "General Information",
          description: JSONResponse.message,
        }
      }));
    }

  }
  service.addListener(showNotification);
})();
