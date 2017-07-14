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

var Utils = {
  getDashboardId: function() {
    var dashboard = document.location.hash;
    if (dashboard && dashboard.length > 1) {
      dashboard = dashboard.substring(1);
    } else {
      var searchParams = new URLSearchParams(window.location.search);
      dashboard = searchParams.get('board');
    }
    return dashboard || '';
  },

  getWorkingDays: function(start, end) {
    var diff = 0;
    start = moment(start);
    end = moment(end);
    if (start.isBefore(end)) {
      end.add(-1, 'days');
      diff = moment().isoWeekdayCalc({
        rangeStart: start,
        rangeEnd: end,
        weekdays: [1, 2, 3, 4, 5],
        exclusions: [],
        inclusions: []
      });
    }
    return diff;
  },


  raiseEvent: function(source, detail) {
    source.dispatchEvent(
        new CustomEvent('dashboard-updated', {bubbles: true, detail: detail}));
  },

  toClassName: function(s) { return s && s.toLowerCase().replace(' ', '-').replace('_','-'); }
};

rivets.formatters.dateFrom = function(value, now) {
  return moment(value).from(now);
};

rivets.formatters.date = function(value, now) {
  return moment(value).calendar(now);
};

rivets.formatters.textCleanUp = function(value) {
  return decodeURIComponent(decodeURIComponent(value));
};

rivets.formatters.duration = function(value) {
  if(!value) {
    return "";
  }
  var duration = moment.duration(value);
  var rt = "" + duration.seconds() + " sec";
  if(duration.asMinutes() >= 1) {
    if(duration.seconds() < 1) {
      rt = "";
    } else {
      rt = " " + rt;
    }
    rt = "" + Math.round(duration.asMinutes()) + " min" + rt;
  }
  return rt;
};

rivets.binders['pclass-*'] = function(el, value) {
  var prefix = this.args[0] + '-';
  var $el = $(el);
  var classList = el.className.split(/\s+/);
  classList.forEach(function(name) {
    if (name.startsWith(prefix)) {
      $el.removeClass(name);
    }
  });
  if (value) {
    $(el).addClass(prefix + Utils.toClassName(value));
  }
};

function styleBuilder(style, suffix) {
  suffix = suffix || '';

  return function(el, value) {
    $(el).css(style, '' + value + suffix);
  };
}

rivets.binders.width = styleBuilder('width','%');
rivets.binders.left = styleBuilder('left','%');
rivets.binders.right = styleBuilder('right','%');

rivets.binders.color = function(el, value) {
  $(el).css('color', '#' + value);
};
