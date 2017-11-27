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
  function Tile() {
    return Reflect.construct(DashboardComponent, [], new.target);
  }

  Object.setPrototypeOf(Tile.prototype, DashboardComponent.prototype);
  Object.setPrototypeOf(Tile, DashboardComponent);

  Tile.prototype.connectedCallback = function() {
    this.forceEnabled = this.getAttribute('enabled') === 'true';
    if(this.getAttribute('config')) {
      this.getModel().config = this.getAttribute('config');
    }
    return DashboardComponent.prototype.connectedCallback.call(this, arguments);
  };

  Tile.prototype._dispose = function() {
    if(!this._inited) {
      return;
    }
    this._inited = false;

    if (this.controller && this.controller.dispose) {
      this.controller.dispose();
    }
    this.onDispose();
  };

  Tile.prototype.getDashboardId = function() {
    return this.getConfig() && this.getConfig().name;
  };

  // Fires when an instance of the element is created
  Tile.prototype._init = function() {
    var config = this.getConfig();
    if (!this._inited && config) {
      var CtrlClass = this.getControllerClass();
      if(CtrlClass) {
        this.controller = new CtrlClass(this.getDashboardId());

        if(this.controller.observable) {
          this.controller.observable.attach(function (data) {
            if(data) {
              this.getModel().updatedDate = Date.now();
            }
            this.refresh(data);
          }.bind(this));
        }

        var promise = this.controller.init(config) || Promise.resolve(true);
        return promise.then(function () {
          this._inited = true;
          this._setEnabled('true');
          this._resolveBootstrapping();
          return true;
        }.bind(this)).catch(function (err) {
          if(err) {
            console.error(err);
          }
          Utils.raiseEvent(this,{});
          this._setEnabled('false');
          return false;
        }.bind(this));
      } else {
        this._inited = true;
        this._setEnabled('true');
        this.refresh(this.getConfig());
        this._resolveBootstrapping();
        return Promise.resolve(true);
      }
    }
    return Promise.resolve(false);
  };

  Tile.prototype.bootstrap = function () {
    return DashboardComponent.prototype.bootstrap.call(this, arguments).then(function () {
      return this._inited ? Promise.resolve() : new Promise(function (resolve, reject) {
        this._init().then(function (loaded) {
          if(loaded) {
            window.addEventListener('dashboard-updated', function() {
              if(this.isEnabled()){
                setTimeout(this._computeSize.bind(this));
              }
            }.bind(this));
            resolve();
          } else {
            this._awaitingBootstrapPromise = {
              resolve: resolve,
              reject: reject
            };
          }
        }.bind(this));
      }.bind(this)).then(function () {
        this._awaitingBootstrapPromise = undefined;
      }.bind(this));
    }.bind(this)).then(this.onInit.bind(this));
  };

  Tile.prototype._resolveBootstrapping = function () {
    if(this.getConfig() && this._awaitingBootstrapPromise) {
      this._awaitingBootstrapPromise.resolve();
    }
  };

  Tile.prototype.getConfig = function() {
    return this.getModel().config && JSON.parse(this.getModel().config);
  };

  Tile.prototype.isEnabled = function () {
    return this.getAttribute('enabled') !== 'false' || this.forceEnabled;
  };

  Tile.prototype._setEnabled = function(value) {
    if(this.getAttribute('enabled') !== value) {
      this.setAttribute('enabled', value);
    }
  };

  Tile.prototype._processEnabled = function() {
    if (!this.isEnabled()) {
      this._dispose();
    } else {
      this._init();
    }
  };

  Tile.prototype._processConfig = function() {
    if (this.isEnabled()) {
      this._dispose();
      this._init();
    }
  };

  Tile.observedAttributes = ['config','pid','enabled'];

  Tile.prototype.attributeChangedCallback = function(
      attributeName, oldValue, newValue, namespace) {
    DashboardComponent.prototype.attributeChangedCallback.call(this, arguments);
    switch (attributeName) {
      case 'enabled':
        this._processEnabled();
        break;
      case 'config':
        this.getModel().config = newValue;
        this._processConfig();
        break;
    }
  };

  Tile.prototype.getControllerClass = function() {
    return this.controllerClass;
  };

  Tile.prototype.render = function() {
    throw 'Render not implemented';
  };

  Tile.prototype.refresh = function (data) {
    DashboardComponent.prototype.refresh.call(this, arguments);
    this._prevData = data ? Utils.clone(data): this._prevData;
    if(this.__pending_refresh) return;
    this.__pending_refresh = setTimeout(function () {
      this.__pending_refresh = undefined;
      if(this._prevData) {
        this.render(Utils.clone(this._prevData));
      }
    }.bind(this));
  };

  Tile.prototype.onInit = function () {};
  Tile.prototype.onDispose = function () {};

  customElements.define('tile-component', Tile);

  return Tile;

})();
