var seleniumHost = process.env.SELENIUM_HOST || 'localhost';
var seleniumPort = process.env.SELENIUM_PORT || '4444';
var appHost = process.env.APP_HOST || 'localhost';
var appPort = process.env.APP_PORT || '8080';

var config = {

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
        firefox: {
            browserName: 'firefox',
            name: 'MirrorGate Firefox'
        },
        chrome: {
            browserName: 'chrome',
            name: 'MirrorGate Chrome'
        }
    }[process.env.BROWSER || 'chrome'],

    // Framework to use. Jasmine 2 is recommended.
    framework: 'jasmine2',

    baseUrl: 'http://' + appHost + ':' + appPort + '/',

    // Spec patterns are relative to the current working directly when
    // protractor is called.
    specs: ['specs/**/*.js'],

    seleniumAddress: 'http://'+seleniumHost+':' + seleniumPort + '/wd/hub',

    onPrepare: function(){
        browser.waitForAngularEnabled(false);
    },
};

exports.config = config;