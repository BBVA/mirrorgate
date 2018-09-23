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
const server = require('karma').server;
const selenium = require('selenium-standalone');
const sass = require('gulp-sass');
const sourcemaps = require('gulp-sourcemaps');
const useref = require('gulp-useref');
const gulpif = require('gulp-if');
const minifyCss = require('gulp-clean-css');
const glob = require('glob');
const filter = require('gulp-filter');
const path = require('path');
const fs = require('fs');
const rev = require('gulp-rev');
const revReplace = require('gulp-rev-replace');
const revFormat = require('gulp-rev-format');

var uglifyjs = require('uglify-js');
var minifier = require('gulp-uglify');

const sassOptions = {
  errLogToConsole: true,
  outputStyle: 'compressed'
};

const paths = {
  src: ['src/**/*', '!src/**/*.spec.*', '!src/sass/**/*'],
  dist: 'dist/',
  target: 'target/',
  libs: [
    'node_modules/bootstrap/dist/js/*.min.*',
    'node_modules/bootstrap/dist/css/*.min.*',
    'node_modules/resize-observer-polyfill/dist/ResizeObserver.global.js',
    'node_modules/@webcomponents/webcomponentsjs/bundles/webcomponents-sd-ce.js',
    'node_modules/jquery/dist/*.min.*',
    'node_modules/jquery.dotdotdot/dist/jquery.dotdotdot.js',
    'node_modules/moment/min/*.min.*',
    'node_modules/moment-weekday-calc/build/*.min.*',
    'node_modules/d3/*.min.*',
    'node_modules/rivets/dist/*.min.*',
    'node_modules/typeahead.js/dist/*.min.*'
  ],
  fonts: [
    'node_modules/roboto-fontface*/css/roboto/roboto-fontface.css',
    'node_modules/roboto-fontface*/fonts/**/*',
    'node_modules/font-awesome*/css/font-awesome.min.css',
    'node_modules/font-awesome*/fonts/**/*'
  ]
};

gulp.task('clean', () => gulp.src(paths.dist + "*", {read: false}).pipe(clean()));

gulp.task('lint', function() {
  return gulp.src('./src/**/*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('default'))
    .pipe(jshint.reporter('fail'));
});

gulp.task(':build', (cb) => {
  gulp.src(paths.src).pipe(gulp.dest(paths.dist));
  gulp.src(paths.libs).pipe(gulp.dest(paths.dist + '/libs/'));
  gulp.src(paths.fonts).pipe(gulp.dest(paths.dist + '/fonts/'))
    .on('end', () =>
      fs.writeFile(paths.dist + 'versionTag', getVersionId(), cb)
    );
});

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
  return gulp.src('src/sass/**/*')
      .pipe(sourcemaps.init())
      .pipe(sass(sassOptions).on('error', sass.logError))
      .pipe(rename({dirname: ''}))
      .pipe(sourcemaps.write('/'))
      .pipe(gulp.dest('dist/css'));
});

/* To be runned against mocks */
gulp.task('serve', ['build'], () => {
  browserSync.init({port: 8082, server: {baseDir: ['dist', 'test/mocks']}});
  gulp.watch(['src/**/*'], [':serve:watch']);
});

gulp.task('serve:dist', ['dist'], () => {
  browserSync.init({port: 8082, server: {baseDir: ['dist', 'test/mocks']}});
  gulp.watch(['src/**/*'], [':serve:watch']);
});

gulp.task('serve:noclean', [':serve:watch'], () => {
  browserSync.init({port: 8082, server: {baseDir: ['dist', 'test/mocks']}});
  gulp.watch(['src/**/*'], [':serve:watch']);
});

/* To be runned against a local api server */
gulp.task('serve:local', ['build'], () => {
  browserSync.init({port: 3000, server: {baseDir: 'dist'}});
  gulp.watch('src/**/*', [':serve:watch']);
});

gulp.task(
    'test', [':build:sass'], (done) => server.start(
                          {
                            configFile: __dirname + '/karma.conf.js',
                            reporters: ['progress', 'coverage'],
                            preprocessors: {
                              'src/js/**/*.js': ['coverage'],
                              'src/components/**/*.js': ['coverage']
                            },
                            coverageReporter: {type: 'html', dir: 'coverage/'}
                          },
                          done));

gulp.task(
    ':test:watch', [':build:sass'], (done) => server.start(
                                 {
                                   singleRun: false,
                                   autoWatch: true,
                                   configFile: __dirname + '/karma.conf.js',
                                 },
                                 done));

var seleniumServer;

gulp.task('test:local', gulpSequence(':startSelenium', 'test', ':endSelenium'));
gulp.task('test:watch', gulpSequence(':startSelenium', ':test:watch', ':endSelenium'));

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

  //Useref and rev revReplace for js and css
  return glob.sync('dist/**/*.html').reduce((prev, filePath) =>
    prev.then(() => new Promise(function(resolve) {
      const f = filter(['**/*.js','**/*.css'], {restore: true});
      gulp.src(filePath)
        .pipe(useref({base: path.dirname(filePath)}))
        .pipe(gulpif('*.js', minifier({compress: false}, uglifyjs)))
        .on('error', function(err) { console.error(err.toString()); })
        .pipe(gulpif('*.css', minifyCss()))
        .pipe(f)
        .pipe(rev())
        .pipe(revFormat({prefix:'-reved-'}))
        .pipe(f.restore)
        .pipe(revReplace())
        .pipe(gulp.dest(path.dirname(filePath)))
        .on('end', resolve);
    }))
  , Promise.resolve()).then(() => {
    //Rev and RevReplace for html references... they are relative so lots of work to do :-(
      const f = filter(['dist/components/**/*.html'], {restore: true});
      return gulp.src('dist/**/*.html')
        .pipe(f)
        .pipe(rev())
        .pipe(revFormat({prefix:'-reved-'}))
        .pipe(f.restore)
        .pipe(gulp.dest('dist'))
        .pipe(rev.manifest())
        .pipe(gulp.dest('dist'))
        .on('end', () => {
          return glob.sync('dist/**/*.html').reduce((prev, filePath) => {
            var manifest = gulp.src("dist/rev-manifest.json");
            var base = path.resolve(path.dirname(filePath));
            var relativeToCurrent = (f) => {
              var value = path.relative(base, path.resolve(path.join('dist', f)));
              //This is a hack to avoid replacing the own html name globally in the file
              if(value.indexOf(path.sep) < 0) {
                value = f;
              }
              return value;
            }
            return prev.then(() => new Promise(function(resolve) {
              gulp.src(path.basename(filePath), {cwd:path.dirname(filePath)})
                .pipe(revReplace({
                  manifest: manifest,
                  modifyUnreved: relativeToCurrent,
                  modifyReved: relativeToCurrent
                }))
                .pipe(gulp.dest(path.dirname(filePath)))
                .on('end', resolve);
            }));
          }, Promise.resolve());
        });
  });
});

gulp.task('default', gulpSequence('build', 'test:local'));
gulp.task('dist', gulpSequence('build', 'html'));

function getVersionId() {
  let version = new Date().toISOString();
  if(process.env.GIT_BRANCH) {
      version += ' - ' + process.env.GIT_BRANCH;
  }
  if(process.env.BUILD_NUMBER) {
    version += ' - ' + process.env.BUILD_NUMBER;
  }
  return version;
}
