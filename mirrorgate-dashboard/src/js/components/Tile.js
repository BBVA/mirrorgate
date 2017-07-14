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
  var TilePrototype = Object.create(HTMLElement.prototype);

  TilePrototype.createdCallback = function() {
    // Creates the shadow root
    var shadowRoot = this.createShadowRoot();

    // Add the image to the Shadow Root.
    this._rootElement = document.importNode(this.getTemplate().content, true);
    shadowRoot.appendChild(this._rootElement);

    // Rivets templating capabilities initialization
    this.model = {};
    setTimeout(function() {
      rivets.bind($(shadowRoot), this.model);
    }.bind(this));

    this.addEventListener('dashboard-updated', function(e) {
      d3.select(this).classed({
        'module-error': e.detail.status === 'error',
        'module-warning': e.detail.status === 'warn',
        'module-ok': e.detail.status === 'ok',
        'module-empty': e.detail.status === 'empty',
        'module-data-error': e.detail.status === 'server-error'
      });
      this._computeSize();
    });

    window.addEventListener('resize', function() {
      setTimeout(this._computeSize.bind(this));
    }.bind(this));

    this._processEnabled();
    this._computeSize.bind(this);
  };

  TilePrototype._computeSize = function() {
    var style = window.getComputedStyle(this);
    var width = parseFloat(style.width.substring(0, style.width.length - 2)),
        height = parseFloat(style.height.substring(0, style.height.length - 2));
    var min = Math.min(width, height);

    if (isNaN(min)) {
      setTimeout(this._computeSize.bind(this), 1);
    } else {
      var classes = {
        'tile-s': min < 300,
        'tile-m': min >= 300 && min <= 600,
        'tile-l': min > 600
      };
      d3.select(this).classed(classes);
    }

  };

  TilePrototype._dispose = function() {
    if(!this._inited) {
      return;
    }
    this._inited = false;

    if (this.controller) {
      this.controller.dispose();
    }
    this.onDispose();
  };

  TilePrototype.getModel = function() { return this.model; };

  TilePrototype.getDashboardId = function() {
    return this.getAttribute('pid');
  };

  TilePrototype.getConfig = function() {
    var config = this.getAttribute('pconfig');
    return config && JSON.parse(config);
  };

  // Fires when an instance of the element is created
  TilePrototype._init = function() {
    if(this._inited) {
      return;
    }
    this._inited = true;
    var config = this.getConfig();
    if (typeof(this.getDashboardId()) === 'string' && config) {
      this.controller = new (this.getControllerClass())(this.getDashboardId());
      this.controller.observable.attach(function (data) {
        if(data) {
          this.getModel().updatedDate = Date.now();
        }
        this.render(data);
      }.bind(this));
      this.controller.init(config);
    }
    this.onInit();
  };

  TilePrototype._processEnabled = function() {
    if (this.getAttribute('enabled') === 'true') {
      this._init();
    } else {
      this._dispose();
    }
  };

  TilePrototype._processPid = function() {
    if (this.getAttribute('enabled') === 'true') {
      this._dispose();
      this._init();
    }
  };

  TilePrototype.attributeChangedCallback = function(
      attributeName, oldValue, newValue, namespace) {
    switch (attributeName) {
      case 'enabled':
        this._processEnabled();
        break;
      case 'pid':
      case 'pconfig':
        this._processPid();
        break;
    }
  };

  TilePrototype.render = function() { throw 'Render not implemented'; };

  TilePrototype.getTemplate = function() {
    throw 'getTemplate not implemented';
  };

  TilePrototype.getControllerClass = function() {
    throw 'getControllerClass not implemented';
  };

  TilePrototype.onInit = function () {};
  TilePrototype.onDispose = function () {};

  return TilePrototype;

})();
