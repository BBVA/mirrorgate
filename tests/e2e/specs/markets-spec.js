'use strict';

const until = protractor.ExpectedConditions;

describe('markets-components', function () {

    beforeAll(function () {
        browser.get('mirrorgate/canary.html?board=mirrorgate&lightDOM');
    });

    beforeEach(function() {
        browser.wait(until.presenceOf(element(by.css('markets-tile .applications'))));
    });

    it('should display last days rates by platform', function () {
        expect(element.all(by.css('markets-tile .Android .rate__total .fa-star')).count()).toBe(4);
        expect(element.all(by.css('markets-tile .Android .rate__total .fa-star-o')).count()).toBe(1);
        expect(element.all(by.css('markets-tile .IOS .rate__total .fa-star')).count()).toBe(4);
        expect(element.all(by.css('markets-tile .IOS .rate__total .fa-star-o')).count()).toBe(1);
    });

    it('should display last days votes by platform', function () {
        expect(element.all(by.css('markets-tile .Android .rate__tag .value')).getText()).toContain('1');
        expect(element.all(by.css('markets-tile .IOS .rate__tag .value')).getText()).toContain('1');
    });

    it('should display total rates by platform', function () {
        expect(element.all(by.css('markets-tile .Android .rate__total-data .fa-star')).count()).toBe(3);
        expect(element.all(by.css('markets-tile .Android .rate__total-data .fa-star-o')).count()).toBe(1);
        expect(element.all(by.css('markets-tile .Android .rate__total-data .fa-star-half-o')).count()).toBe(1);
        expect(element.all(by.css('markets-tile .IOS .rate__total-data .fa-star')).count()).toBe(4);
        expect(element.all(by.css('markets-tile .IOS .rate__total-data .fa-star-o')).count()).toBe(0);
        expect(element.all(by.css('markets-tile .IOS .rate__total-data .fa-star-half-o')).count()).toBe(1);
    });

    it('should display total votes by platform', function () {
        expect(element.all(by.css('markets-tile .Android .rate-legend-votes .value')).getText()).toContain('20');
        expect(element.all(by.css('markets-tile .IOS .rate-legend-votes .value')).getText()).toContain('10');
    });

});
