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

function httpGetAsync(url, callback, options) {
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.onreadystatechange = function() {
    if (xmlHttp.readyState === 4) {
      if (xmlHttp.status === 200) {
        callback(null, xmlHttp.responseText);
      } else if (xmlHttp.status >= 400) {
        callback({
          status: xmlHttp.status,
          message: 'Server error'
        });
      }
    }
  };
  xmlHttp.onerror = function(err) { callback(err); };

  xmlHttp.open('GET', url, true);  // true for asynchronous

  if(options && options.headers) {
    for(var header of options.headers) {
      xmlHttp.setRequestHeader(header.name,header.value);
    }
  }

  xmlHttp.send(null);
}
