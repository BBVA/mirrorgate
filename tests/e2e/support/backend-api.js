

const request = require('request');

const MIRRORGATE_ENDPOINT = 'http://' + (process.env.APP_HOST || 'localhost') + ':8080/mirrorgate';

function lockUntilCompletion() {
    if(typeof(browser) !== 'undefined') {
        return browser.executeAsyncScript(function (cb) {
            ServerSentEvent.addListener(function listener(eventType) {
                if(eventType !== 'PingType') {
                    ServerSentEvent.removeListener(listener);
                    testability.when.ready(cb);
                }
            }, true);
        });
    }
}

function send(endpoint, data, params) {
    var qs = '';

    if(params) {
        qs = '?';
        Object.keys(params).forEach(function(key) {
            qs += key + '=' + params[key];
        }, this);
    }

    return new Promise((resolve, reject) => {
        request.post(MIRRORGATE_ENDPOINT + '/api/' + endpoint + qs,
        {
          headers: {
            'content-type': 'application/json',
          },
          body: JSON.stringify(data)
        },
        (err, res, body) => {
          if (err) {
            reject(err);
            return;
          }

          if(res.statusCode >= 400) {
            reject({
                statusCode: res.statusCode,
                statusMessage: res.statusMessage
            });
            return;
          }
          resolve(body);
        });
    });

}

var API = {
    __pendingChanges: [],
    restore: function () {
        let promises = [];
        while(API.__pendingChanges.length > 0) {
            promises.push(API.__pendingChanges.pop().restore());
        }
        return Promise.all(promises);
    },
    stories: {
        send: function (original, patch) {
            var toSend = Object.assign({}, original, patch);
            var promise = send('issues', [toSend], {
                collectorId: "mirrorgate-collectors-jira"
            });
            API.__pendingChanges.push({
                restore: function () {
                    return send('issues', [original], {
                        collectorId: "mirrorgate-collectors-jira"
                    }).then(lockUntilCompletion);
                }
            });
            return promise.then(function () {
                return lockUntilCompletion();
            });
        }
    },

};

module.exports = API;
