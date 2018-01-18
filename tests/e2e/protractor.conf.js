var seleniumHost = process.env.SELENIUM_HOST || 'localhost';
var appHost = process.env.APP_HOST || 'localhost';

var config = {
    directConnect: false,

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
      'browserName': 'chrome'
    },

    // Framework to use. Jasmine 2 is recommended.
    framework: 'jasmine2',

    baseUrl: 'http://' + appHost + ':8080/',

    plugins: [{
        package: 'protractor-testability-plugin'
    }],

    // Spec patterns are relative to the current working directly when
    // protractor is called.
    specs: ['specs/**/*.js'],

    // Options to be passed to Jasmine.
    jasmineNodeOpts: {
      defaultTimeoutInterval: 30000
    },

    seleniumAddress: 'http://'+seleniumHost+':4444/wd/hub',

    onPrepare: function(){
        browser.manage().timeouts().implicitlyWait(0);
    },
  };

  //They share and modify the DB... cant be parallelized
    config.capabilities= {
        firefox: {
            'browserName': 'firefox',
            name: 'mirrorgate Firefox',
        },
        chrome: {
            'browserName': 'chrome',
            name: 'mirrorgate Chrome'
        }
    }[process.env.BROWSER || 'chrome'];

  exports.config = config;