'use strict';

const api = require('../support/backend-api');
const data = require('../support/fixtures');
const until = protractor.ExpectedConditions;

describe('current-sprint', function () {

    beforeAll(function () {
        browser.get('mirrorgate/canary.html?board=mirrorgate&lightDOM');
    });

    beforeEach(function() {
        browser.wait(until.presenceOf(element(by.css('current-sprint-tile.module-error'))));
    });

    it('should display stories by status', function () {
        expect(element.all(by.css('current-sprint-tile .arc path.status-in-progress')).count()).toBe(3);
        expect(element.all(by.css('current-sprint-tile .arc path.status-done')).count()).toBe(2);
        expect(element.all(by.css('current-sprint-tile .arc path.status-impeded')).count()).toBe(2);
        expect(element.all(by.css('current-sprint-tile .arc path.status-backlog')).count()).toBe(5);
    });

    it('should display rate completed', function () {
        expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('17');
    });

    it('should alert spring advance risks', function () {
        expect(element(by.css('current-sprint-tile.module-ok')).isPresent()).toBeFalsy();
    });

    it('should change the completion amount and reflect status change', function () {
        expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('17')
        data.stories.backlog.forEach((story) => api.stories.send(story, {status:'DONE'}));
        browser.wait(until.presenceOf(element(by.css('current-sprint-tile.module-ok'))));
        expect(element(by.css('current-sprint-tile div[rv-show="sprint.doneRatio"] .rate-completed')).getText()).toContain('70');
        expect(element.all(by.css('current-sprint-tile .arc path.status-in-progress')).count()).toBe(3);
        expect(element.all(by.css('current-sprint-tile .arc path.status-backlog')).count()).toBe(0);
    });

});
