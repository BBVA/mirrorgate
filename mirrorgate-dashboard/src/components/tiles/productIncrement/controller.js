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
 * ProductIncrementController controller to show product increment information
 */

var ProductIncrementController = (
  function(dashboardId){
    var observable = new Event('ProductIncrementController');
    var service = Service.get(Service.types.productincrement, dashboardId);

    function getProductIncrement(response) {
      var productIncrement;

      if(response) {
        productIncrement = new ProductIncrement(JSON.parse(response));
      }

      observable.notify(productIncrement);

    }

    this.observable = observable;
    this.dispose = function() {
      this.observable.reset();
      service.removeListener(getProductIncrement);
    };
    this.init = function() {
      service.addListener(getProductIncrement);
    };
  }
);
