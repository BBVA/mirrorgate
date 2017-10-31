
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

var Service = (function() {
  'use strict';

  function Service(serviceType, dashboardId) {
    var url = serviceType.getUrl(dashboardId);
    var timer = serviceType.timer;

    var event = new Event(this);
    var pending = false;

    var attempt = 1;
    var breakTimeout;

    function callHttpService() {
      if (pending) {
        return;
      }
      pending = true;
      httpGetAsync(url, function(err, data) {

        if(err) {
          if(breakTimeout) {
            clearTimeout(breakTimeout);
          }
          breakTimeout = setTimeout(function() {
            attempt++;
            breakTimeout = undefined;
            callHttpService();
          }, Math.min(32000, Math.pow(2, attempt) * 1000));
        } else {
          attempt = 1;
        }

        pending = false;
        event.notify(data);
      });
    }

    function processServerEvent(serverEventType){
      var serverSentEventtype=JSON.parse(serverEventType);
      if(serverSentEventtype.type === serviceType.serverEventType){
        callHttpService();
      }
    }

    this.addListener = function(callback) {
      event.attach(callback);
      this._checkEventRegistration();
      this.request();
    };

    this.removeListener = function(callback) {
      event.detach(callback);
      this._checkEventRegistration();
    };

    this.request = function() { callHttpService(); };

    this._checkEventRegistration = function() {
      if (event.getListeners().length && !this._attached) {
        this._attached = true;
        if(serviceType.serverEventType){
          ServerSentEvent.addListener(processServerEvent);
        }
        timer.attach(callHttpService);
      } else if (!event.getListeners().length && this._attached) {
        this._attached = false;
        if(serviceType.serverEventType){
          ServerSentEvent.removeListener(processServerEvent);
        }
        timer.detach(callHttpService);
      }
    };

    this.dispose = function() {
      event.reset();
      this._checkEventRegistration();
    };
  }

  function ServiceType(timer, resource, serverEventType) {
    this._instances = {};
    this.timer = timer;
    this.serverEventType = serverEventType;
    this.getUrl = function(dashboardId) {
      var url = 'dashboards/';
      if (dashboardId) {
        url += dashboardId + '/';
        if (resource) {
          url += resource;
        }
      }
      return url;
    };
  }

  var self = {
    types: {
      builds: new ServiceType(Timer.eventually, 'builds','BuildType'),
      bugs: new ServiceType(Timer.eventually, 'bugs'),
      stories: new ServiceType(Timer.rarely, 'stories', 'FeatureType'),
      apps: new ServiceType(Timer.rarely, 'applications', 'ReviewType'),
      dashboard: new ServiceType(Timer.never, 'details'),
      dashboards: new ServiceType(Timer.never),
      programincrement: new ServiceType(Timer.eventually, 'programincrement', 'FeatureType'),
      notifications: new ServiceType(Timer.never, 'notifications'),
      userMetrics: new ServiceType(Timer.eventually, 'user-metrics'),
    },
    get: function(type, dashboardId) {
      return (
          type._instances[dashboardId] =
              type._instances[dashboardId] || new Service(type, dashboardId));
    },
    reset: function() {
      for (var type in self.types) {
        var item = self.types[type];
        for (var board in item._instances) {
          item._instances[board].dispose();
        }
        item._instances = {};
      }
    }
  };

  return self;

})();
