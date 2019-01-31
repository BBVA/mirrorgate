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

        if(e.detail.status) {
          d3.select(this)
            .classed('module-error', e.detail.status === 'error')
            .classed('module-warning', e.detail.status === 'warn')
            .classed('module-ok', e.detail.status === 'ok')
            .classed('module-empty', e.detail.status === 'empty')
            .classed('module-data-error', e.detail.status === 'server-error');
        }

        if(this.classList.contains('keep-height')) {
          setTimeout(function () {
            var contents = this.getRootElement().querySelector('.component__body');
            if(contents) {
              var height = contents.offsetHeight + contents.offsetTop - contents.parentElement.offsetTop;

              this.parentElement.style["min-height"]=height + 'px';
              this.parentElement.style.height='auto';
            }
          }.bind(this));
        }
      }.bind(this));

      this._resizeObserver = new ResizeObserver(this._computeSize.bind(this));
      this._resizeObserver.observe(this);

    }.bind(this));
  };

  DashboardComponent.prototype._computeSize = function() {
    var style = window.getComputedStyle(this);
    var width = parseFloat(style.width.substring(0, style.width.length - 2)),
        height = parseFloat(style.height.substring(0, style.height.length - 2));
    var min = Math.min(width, height);

    d3.select(this)
      .classed('tile-s', min < 300)
      .classed('tile-m', min >= 300 && min <= 600)
      .classed('tile-l', min > 600);

    if(!isNaN(min) && (this._width !== width || this._height !== height)) {
      this.dispatchEvent(
        new CustomEvent('component-resized', {bubbles: false, detail: {
          width: width,
          height: height
        }}));

      this._width = width;
      this._height = height;

      this.refresh();
    }
  };

  DashboardComponent.prototype.refresh = function () {
  };

  customElements.define('dashboard-inner-component', DashboardComponent);
  return DashboardComponent;

})();
