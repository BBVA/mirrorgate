/**
 *    Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

(function () {

  //Checks for front updates every 60 secconds. If changes, then refresh.
  var version;

  setInterval(function () {
    httpGetAsync('versionTag', function (err, newVersion) {
      if(!err && newVersion && version && version !== newVersion) {
        //Wait 10 secconds to let the server warm up
        setTimeout(function () {
          window.location.reload(true);
        }, 10000);
      }
      version = newVersion;
    });
  }, 60000);

})();
