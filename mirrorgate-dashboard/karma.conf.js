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

// Karma configuration
// Generated on Wed Jun 24 2015 17:14:26 GMT+0200 (Romance Daylight Time)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine', 'sinon'],


    // list of files / patterns to load in the browser
    files: [
      'bower_components/webcomponentsjs/webcomponents-hi-sd-ce.js',
      'bower_components/d3/d3.min.js',
      'bower_components/jquery/dist/jquery.slim.min.js',
      'bower_components/rivets/dist/rivets.bundled.min.js',
      'bower_components/moment/min/moment.min.js',
      'bower_components/moment-weekday-calc/build/moment-weekday-calc.min.js',
      'bower_components/jQuery.dotdotdot/dist/jquery.dotdotdot.js',
      'node_modules/lodash/lodash.min.js',
      'node_modules/karma-read-json/karma-read-json.js',
      'test/*.js',
      'src/js/core/utils.js',
      'src/js/components/BaseComponent.js',
      'src/js/components/DashboardComponent.js',
      'src/js/components/Tile.js',
      'src/js/core/Event.js',
      'src/js/core/Clock.js',
      'src/js/core/Timer.js',
      'src/js/core/Service.js',
      'src/js/core/request.js',
      'src/js/core/ServerSentEvent.js',
      'src/**/*.spec.*',
      'src/components/**/*.*',
      {pattern: 'test/**/*', included: false},
      {pattern: 'dist/img/**/*.*', included: false, served: true},
      {pattern: 'dist/bower_components/**/*.css', included: false, served: true},
      {pattern: 'dist/css/*.css', included: false, served: true},
      {pattern: 'bower_components/*/fonts/**/*.*', included: false, served: true}
    ],


    // list of files to exclude
    exclude: [
      'src/js/app.js'
    ],

    proxies: {
      '/css/': '/base/dist/css/',
      '/img/': '/base/dist/img/',
      '/node_modules/': '/base/node_modules/',
      '/bower_components/': '/base/dist/bower_components/',
      '/test/': '/base/test/'
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],  // coverage reporter generates the coverage


    // karma server port
    port: 9876,

    hostname: process.env.HOSTNAME || 'localhost',


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_DEBUG,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,


    // custome launch for WebDriver
    customLaunchers: {
      'chrome': {
        base: 'WebDriver',
        config: {
          port: process.env.SELENIUM_PORT_CHROME || 4444,
          hostname: process.env.SELENIUM_HOST_CHROME || 'localhost'
        },
        browserName: 'chrome'
      },
      'firefox': {
        base: 'WebDriver',
        config: {
          port: process.env.SELENIUM_PORT_FIREFOX || 4444,
          hostname: process.env.SELENIUM_HOST_FIREFOX || 'localhost'
        },
        browserName: 'firefox'
      }
    },


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['chrome','firefox'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: true,

    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity,

    // No activity browser Timeout
    browserNoActivityTimeout: 10000

  });
};
