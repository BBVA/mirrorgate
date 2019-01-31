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
const Server = require('karma').Server;
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
var minifier = require('gulp-uglify-es').default;

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
    'node_modules/@webcomponents/webcomponentsjs/webcomponents-loader.js',
    'node_modules/@webcomponents/html-imports/html-imports.min.js',
    'node_modules/jquery/dist/*.min.*',
    'node_modules/jquery.dotdotdot/dist/jquery.dotdotdot.js',
    'node_modules/moment/min/*.min.*',
    'node_modules/moment-weekday-calc/build/*.min.*',
    'node_modules/d3/dist/*.min.*',
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

/* Sass */
gulp.task(':build:sass', () => {
  return gulp.src('src/sass/**/*')
      .pipe(sourcemaps.init())
      .pipe(sass(sassOptions).on('error', sass.logError))
      .pipe(rename({dirname: ''}))
      .pipe(sourcemaps.write('/'))
      .pipe(gulp.dest('dist/css'));
});

gulp.task('build', gulp.series('clean', 'lint', ':build', ':build:sass'));
gulp.task('build:watch', () => {
  gulp.watch(['src/**/*'], gulp.series('build'));
});
gulp.task('build:watch:noclean', () => {
  gulp.watch(['src/**/*'], gulp.series('lint', ':build', ':build:sass'));
});

gulp.task('html', () => {

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

gulp.task('dist', gulp.series('build', 'html'));

function reload(done) {
  browserSync.reload();
  done();
}

function serve(done) {
  browserSync.init({
    port: 8082, server: {baseDir: ['dist', 'test/mocks']}
  });
  done();
}

function serveLocal(done) {
  browserSync.init({
    port: 3000, server: {baseDir: 'dist'}
  });
  done();
}

gulp.task('watch', () => gulp.watch(['src/**/*', 'sass/**/*'], gulp.series('build', reload)));
gulp.task('watch:dist', () => gulp.watch(['src/**/*', 'sass/**/*'], gulp.series('dist', reload)));
gulp.task('watch:noclean', () => gulp.watch(['src/**/*', 'sass/**/*'], gulp.series(':build', ':build:sass', reload)));

gulp.task('serve', gulp.series('build', serve, 'watch'));
gulp.task('serve:dist', gulp.series('dist', serve, 'watch:dist'));
gulp.task('serve:local', gulp.series('build', serveLocal, 'watch'));
gulp.task('serve:noclean', gulp.series(':build', serve, 'watch:noclean'));

gulp.task('test', (done) => {
  server = new Server({
    configFile: __dirname + '/karma.conf.js',
    reporters: ['progress', 'coverage'],
    preprocessors: {
      'src/js/**/*.js': ['coverage'],
      'src/components/**/*.js': ['coverage']
    },
    coverageReporter: {type: 'html', dir: 'coverage/'}
  }, done);
  server.start();
});

gulp.task(':test:watch', (done) => {
  server = new Server({
    singleRun: false,
    autoWatch: true,
    configFile: __dirname + '/karma.conf.js',
  }, done);
  server.start();
});

var seleniumServer;

function startSelenium(done) {
  selenium.start((err, child) => {
    seleniumServer = child;
    done(err);
  });
}

function endSelenium(done) {
  if (seleniumServer) {
    seleniumServer.kill();
    done();
  }
}

gulp.task('test:local', gulp.series(startSelenium, 'build', 'test', endSelenium));
gulp.task('test:watch', gulp.series(startSelenium, 'build', ':test:watch', endSelenium));

gulp.task('default', gulp.series('build', 'test:local'));

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
