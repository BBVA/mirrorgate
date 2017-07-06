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

const gulp = require('gulp');
const jshint = require('gulp-jshint');
const clean = require('gulp-clean');
const rename = require('gulp-rename');
const browserSync = require('browser-sync').create();
const gulpSequence = require('gulp-sequence');
const Server = require('karma').Server;
const watch = require('gulp-watch');
const tar = require('gulp-tar');
const gzip = require('gulp-gzip');
const selenium = require('selenium-standalone');
const sass = require('gulp-sass');
const sourcemaps = require('gulp-sourcemaps');
const useref = require('gulp-useref'), gulpif = require('gulp-if'),

      minifyCss = require('gulp-clean-css'), glob = require('glob');
const path = require('path');

var uglifyjs = require('uglify-js-harmony');
var minifier = require('gulp-uglify/minifier');

const sassOptions = {
  errLogToConsole: true,
  outputStyle: 'compressed'
};

const paths = {
  src: ['src/**/*', '!src/**/*.spec.*', '!src/sass/**/*'],
  dist: 'dist/',
  target: 'target/',
  bower: 'bower_components*/**/*.min.*',
  fonts: [
    'bower_components*/roboto-fontface/css/roboto/roboto-fontface.css',
    'bower_components*/roboto-fontface/fonts/**/*',
    'bower_components*/font-awesome/css/font-awesome.css',
    'bower_components*/font-awesome/fonts/**/*'
  ]
};

gulp.task('clean', () => gulp.src(paths.dist, {read: false}).pipe(clean()));

gulp.task('lint', function() {
  return gulp.src('./src/**/*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('default'))
    .pipe(jshint.reporter('fail'));
});

gulp.task(
    ':build',
    () => gulp.src(paths.src.concat([paths.bower]).concat(paths.fonts))
              .pipe(gulp.dest(paths.dist)));

gulp.task('build', gulpSequence('clean', 'lint', ':build', ':build:sass'));
gulp.task('build:watch', ['build'], () => {
  gulp.watch(['src/**/*'], [':build', ':build:sass']);
});

gulp.task('build:watch:noclean', [], () => {
  gulp.watch(['src/**/*'], ['lint', ':build', ':build:sass']);
});

gulp.task(
    ':serve:watch', [':build', ':build:sass'], () => browserSync.reload());

/* Sass */
gulp.task(':build:sass', function() {
  return gulp.src('src/sass/*')
      .pipe(sourcemaps.init())
      .pipe(sass(sassOptions).on('error', sass.logError))
      .pipe(sourcemaps.write('/'))
      .pipe(gulp.dest('dist/css'));
});

/* To be runned against mocks */
gulp.task('serve', ['build'], () => {
  browserSync.init({port: 8080, server: {baseDir: ['dist', 'test/mocks']}});
  gulp.watch(['src/**/*'], [':serve:watch']);
});

/* To be runned against a local api server */
gulp.task('serve:local', ['build'], () => {
  browserSync.init({port: 3000, server: {baseDir: 'dist'}});
  gulp.watch('src/**/*', [':serve:watch']);
});

gulp.task(
    'test', (done) => new Server(
                          {
                            configFile: __dirname + '/karma.conf.js',
                            reporters: ['progress', 'coverage'],
                            preprocessors: {
                              'dist/js/**/*.js': ['coverage'],
                              'dist/components/**/*.js': ['coverage']
                            },
                            coverageReporter: {type: 'html', dir: 'coverage/'}
                          },
                          done)
                          .start());

gulp.task(
    ':test:watch', (done) => new Server(
                                 {
                                   singleRun: false,
                                   autoWatch: true,
                                   configFile: __dirname + '/karma.conf.js',
                                 },
                                 done)
                                 .start());

var seleniumServer;

gulp.task('test:local', gulpSequence(':startSelenium', 'test', ':endSelenium'));
gulp.task(
    'test:watch',
    gulpSequence(':startSelenium', ':test:watch', ':endSelenium'));

gulp.task(':startSelenium', (done) => {
  selenium.start((err, child) => {
    seleniumServer = child;
    done(err);
  });
});

gulp.task(':endSelenium', (done) => {
  if (seleniumServer) {
    seleniumServer.kill();
  }
});

gulp.task('html', function() {

  var promises = [];

  glob.sync('dist/**/*.html').forEach(function(filePath) {
    console.log('Processing: ' + filePath);
    promises.push(new Promise(function(resolve) {
      var pipeline =
          gulp.src(filePath)
              .pipe(useref({base: path.dirname(filePath)}))
              .pipe(gulpif('*.js', minifier({compress: false}, uglifyjs)))
              .on('error', function(err) { console.error(err.toString()); })
              .pipe(gulpif('*.css', minifyCss()))
              .pipe(gulp.dest(path.dirname(filePath)));
      pipeline.on('end', resolve);
    }));
  });
  return Promise.all(promises);
});

gulp.task('default', gulpSequence('build', 'test:local'));
gulp.task('dist', gulpSequence('build', 'html'));
