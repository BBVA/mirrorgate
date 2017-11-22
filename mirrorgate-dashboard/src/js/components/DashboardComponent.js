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

var DashboardComponent = (function() {
'use strict';

  function DashboardComponent() {
    return Reflect.construct(BaseComponent, [], new.target);
  }

  Object.setPrototypeOf(DashboardComponent.prototype, BaseComponent.prototype);
  Object.setPrototypeOf(DashboardComponent, BaseComponent);

  DashboardComponent.prototype.connectedCallback = function() {

    return BaseComponent.prototype.connectedCallback.apply(this).then(function () {
      this.addEventListener('dashboard-updated', function(e) {
        d3.select(this).classed({
          'module-error': e.detail.status === 'error',
          'module-warning': e.detail.status === 'warn',
          'module-ok': e.detail.status === 'ok',
          'module-empty': e.detail.status === 'empty',
          'module-data-error': e.detail.status === 'server-error'
        });
        this._computeSize();
      }.bind(this));

      window.addEventListener('resize', function() {
        setTimeout(this._computeSize.bind(this));
      }.bind(this));
      window.addEventListener('dashboard-updated', function() {
        setTimeout(this._computeSize.bind(this));
      }.bind(this));

      this._computeSize();

    }.bind(this));
  };

  DashboardComponent.prototype._computeSize = function() {
    var style = window.getComputedStyle(this);
    var width = parseFloat(style.width.substring(0, style.width.length - 2)),
        height = parseFloat(style.height.substring(0, style.height.length - 2));
    var min = Math.min(width, height);

    if (isNaN(min)) {
      setTimeout(this._computeSize.bind(this), 1);
    } else if(this._width !== width || this._height !== height) {
      this._width = width;
      this._height = height;

      var classes = {
        'tile-s': min < 300,
        'tile-m': min >= 300 && min <= 600,
        'tile-l': min > 600
      };
      d3.select(this).classed(classes);

      this.dispatchEvent(
        new CustomEvent('component-resized', {bubbles: false, detail: {
          width: width,
          height: height
        }}));

      this.refresh();
    }
  };

  DashboardComponent.prototype.refresh = function () {
  };

  customElements.define('dashboard-inner-component', DashboardComponent);
  return DashboardComponent;

})();
