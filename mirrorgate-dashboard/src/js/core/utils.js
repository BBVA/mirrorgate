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

var Utils = (function() {
  'use strict';

  var breakFavIconTimeout;
  var colorMapping = {
    'daa038' : 'yellow', // Warning status color for Slack
    'd00000': 'red', // Danger status color for Slack
    'red': 'red',
    'yellow': 'yellow'
  };

  function notifyFavIcon(event) {
    if(event && event.detail) {
      console.log(event.detail.color);

      var icon = document.querySelector('link[rel*="icon"]');

      icon.href = 'img/favicon-' + (colorMapping[event.detail.color] || 'blue') + '.png';
      if(breakFavIconTimeout) {
        clearTimeout(breakFavIconTimeout);
      }
      breakFavIconTimeout = setTimeout(function() {
        icon.href = 'img/favicon.png';
        breakFavIconTimeout = undefined;
      }, 60000);
    }
  }

  document.addEventListener('NotificationEvent', notifyFavIcon);
  document.addEventListener('SlackEvent', notifyFavIcon);

  return {
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

    getSkin: function () {
      return this._skin || 'classic';
    },

    initSkin: function () {
      var self = this;
      return new Promise(function (resolve) {
        Service
          .get(Service.types.dashboard, Utils.getDashboardId())
          .addListener(function(details) {
            details = details && JSON.parse(details);
            self._skin = (details && details.skin) || 'classic';
            resolve(self._skin);
          });
      });
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

    toClassName: function(s) { return (s || s === 0) && ('' + s).toLowerCase().replace(' ', '-').replace('_','-'); },

    openDashboard : function(type, boardId) {
      document.location.href = type + '.html?board=' + encodeURIComponent( boardId || Utils.getDashboardId());
      return false;
    },

    openFeedbackForm: function () {
      window.mirrorGateConfig().then(function (config) {
        document.location.href = config.backOfficeUrl + 'index.html#/feedback';
      });
      return false;
    },

    openBackOffice: function (boardId) {
      window.mirrorGateConfig().then(function (config) {
        document.location.href = config.backOfficeUrl + (boardId ?
          'index.html#/edit/' + encodeURIComponent(boardId) + "?backToDashboard=true" :
          'index.html#/list');
      });
      return false;
    },

    browserSupportsShadowDOM : function () {
      return !!window.supportsShadowDOM;
    },

    compareVersions: function (version1, version2, regExp) {

      var v1parts = regExp.exec(version1) || [];
      var v2parts = regExp.exec(version2) || [];

      for (var i = 1; i < Math.max(v1parts.length, v2parts.length); ++i) {
        var part1 = parseInt(v1parts[i]) || 0;
        var part2 = parseInt(v2parts[i]) || 0;

        if (part1 !== part2) {
          return part1 - part2;
        }
      }

      return v1parts.length - v2parts.length;
    },

    rephraseVersion: function (version, regExp) {
      var parts = regExp.exec(version);
      return parts && (parts.length > 1) ? 'v' + parts.slice(1).join('.') : version;
    },

    normalEstimation: function (serie, confidence) {
      serie = serie || [];
      var z = {'80%': 0.84,'90%':1.29, '95%': 1.65, '99%': 2.33}[confidence || '80%'];

      var r = {
          count: 0,
          total: 0,
          avg: 0,
          sigma: 0,
          estimate: 0
      };

      serie.forEach(function(value) {
          r.count++;
          r.total += value;
      }, this);
      r.avg = r.total / r.count;

      serie.forEach(function(value) {
          r.sigma += Math.pow(value - r.avg,2);
      }, this);

      r.sigma = Math.sqrt(r.sigma / (r.total - 1));
      r.estimate = r.sigma * z + r.avg;
      return r;
    },
  };

})();

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

rivets.formatters.round = function(value) {
  return Math.round(value);
};

rivets.formatters.bigNumber = function(value) {
  if(value < 10000) {
    return value;
  } else if(value < 10000000) {
    return Math.round(value / 1000) + 'K';
  } else {
    return Math.round(value / 1000000) + 'M';
  }
};

rivets.formatters.length = function(value) {
  return value ? (value.length || 0) : 0;
};

rivets.formatters['>='] = function (value, arg) {
  return value >= arg;
};

rivets.formatters['='] = function (value, arg) {
  return value === arg;
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
  if (value !== null && value !== undefined) {
    $(el).addClass(prefix + Utils.toClassName(value));
  }
};

rivets.binders['setclass-*'] = function(el, value) {
  var cls = this.args[0];
  var $el = $(el);
  var classList = el.className.split(/\s+/);
  classList.forEach(function(name) {
    if (name === cls) {
      $el.removeClass(name);
    }
  });
  if (value) {
    $(el).addClass(cls);
  }
};

function styleBuilder(style, suffix) {
  suffix = suffix || '';

  return function(el, value) {
    $(el).css(style, '' + value + suffix);
  };
}

rivets.binders.ignore = {
    block: true,
    routine: function () { /* do nothing */ }
};

rivets.binders.width = styleBuilder('width','%');
rivets.binders.left = styleBuilder('left','%');
rivets.binders.right = styleBuilder('right','%');

rivets.binders.color = function(el, value) {
  $(el).css('color', '#' + value);
};
