'use strict';

const api = require('../support/backend-api');
const data = require('../support/fixtures');

describe('mirrorgate', function () {

	describe('sprint', function () {

		beforeAll(function () {
            browser.get('mirrorgate/canary.html?board=mirrorgate&lightDOM');
            browser.executeScript(function () {
                function loadTestability() {
                    if(!window.testability) {
                        setTimeout(loadTestability,1);
                    } else {
                        var wait = window.testability.wait.start();
                        window.addEventListener('WebComponentsReady', wait.end);
                    }
                }
                if(window.WebComponents && !window.WebComponents.ready) {
                    loadTestability();
                }
            });
            browser.executeAsyncScript(function (cb) {
                var container = document.getElementById('main-container');
                if(!container || container.style.visibility !== 'visible') {
                    dashboard = document.querySelector('dashboard-component')
                        .addEventListener('component-ready', cb);
                } else {
                    cb();
                }
            });
            browser.executeAsyncScript(function (cb) {
                document.querySelector('current-sprint-tile').readyPromise().then(cb);
            });

		});

		it('should display stories by status', function () {
            expect(element.all(by.css('current-sprint-tile .outher path.status-in-progress')).count()).toBe(3);
            expect(element.all(by.css('current-sprint-tile .outher path.status-done')).count()).toBe(2);
            expect(element.all(by.css('current-sprint-tile .outher path.status-impeded')).count()).toBe(2);
            expect(element.all(by.css('current-sprint-tile .outher path.status-backlog')).count()).toBe(5);
		});

		it('should display rate completed', function () {
            expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('17');
        });

		it('should change the completion ammount and reflect status change', function (done) {
            expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('17')
                .then(function () {
                    return api.stories.send(data.stories.backlog, {status:'IN_PROGRESS'});
                })
                .then(function() {
                    expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('20');
                    expect(element.all(by.css('current-sprint-tile .outher path.status-in-progress')).count()).toBe(4);
                    expect(element.all(by.css('current-sprint-tile .outher path.status-backlog')).count()).toBe(4);
                    done();
                });
		});

	});
});
