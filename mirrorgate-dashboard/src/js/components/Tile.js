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

var Tile = (function() {
'use strict';

  // Creates an object based in the HTML Element prototype
  var TilePrototype = Object.create(DashboardComponent);

  TilePrototype.createdCallback = function() {
    this.forceEnabled = this.getAttribute('enabled') === 'true';
    this.addEventListener('component-ready', function () {
      this._processEnabled();
    }.bind(this));
    return DashboardComponent.createdCallback.call(this, arguments);
  };

  TilePrototype._dispose = function() {
    if(!this._inited) {
      return;
    }
    this._inited = false;

    if (this.controller && this.controller.dispose) {
      this.controller.dispose();
    }
    this.onDispose();
  };

  // Fires when an instance of the element is created
  TilePrototype._init = function() {
    if(this._inited) {
      return;
    }
    var config = this.getConfig();
    if (config) {
      var CtrlClass = this.getControllerClass();
      if(CtrlClass) {
        this.controller = new CtrlClass(this.getDashboardId());

        if(this.controller.observable) {
          this._inited = true;
          this.controller.observable.attach(function (data) {
            if(data) {
              this.getModel().updatedDate = Date.now();
            }
            this.render(data);
          }.bind(this));
        }

        var promise = this.controller.init(config) || Promise.resolve();
        promise.then(function () {
          this.onInit();
          this.setAttribute('enabled', 'true');
        }.bind(this)).catch(function (err) {
          if(err) {
            console.error(err);
          }
          Utils.raiseEvent(this,{});
          this.setAttribute('enabled', 'false');
        }.bind(this));
      } else {
        this.onInit();
        this.render(this.getConfig());
      }
    }
  };

  TilePrototype._processEnabled = function() {
    if (this.getAttribute('enabled') === 'false' && !this.forceEnabled) {
      this._dispose();
    } else {
      this._init();
    }
  };

  TilePrototype._processConfig = function() {
    if (this.getAttribute('enabled') !== 'false' || this.forceEnabled) {
      this._dispose();
      this._init();
    }
  };

  TilePrototype.attributeChangedCallback = function(
      attributeName, oldValue, newValue, namespace) {
    DashboardComponent.attributeChangedCallback.call(this, arguments);
    if(!this.isReady) {
      return;
    }
    switch (attributeName) {
      case 'enabled':
        this._processEnabled();
        break;
      case 'config':
        this._processConfig();
        break;
    }
  };

  TilePrototype.getControllerClass = function() {
    return this.controllerClass;
  };

  TilePrototype.render = function() {
    throw 'Render not implemented';
  };

  TilePrototype.onInit = function () {};
  TilePrototype.onDispose = function () {};

  return TilePrototype;

})();
