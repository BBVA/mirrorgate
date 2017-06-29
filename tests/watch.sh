#!/usr/bin/env bash

set -e

trap 'kill $DASHBOARD_PID $BACKOFFICE_PID' EXIT

rm -fr logs | true
mkdir logs

pushd ../mirrorgate-api
./gradlew clean build
popd

pushd ../mirrorgate-dashboard
./node_modules/gulp/bin/gulp.js build:watch > ../tests/logs/dashboardboard.log & DASHBOARD_PID=$!
popd

pushd ../mirrorgate-backoffice
./node_modules/webpack/bin/webpack.js --watch > ../tests/logs/backoffice.log & BACKOFFICE_PID=$!
popd

./run.sh

