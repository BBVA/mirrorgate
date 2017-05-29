#!/usr/bin/env bash

set -e

trap 'kill $DASHBOARD_PID $BACKOFFICE_PID' EXIT

rm -fr logs | true
mkdir logs

pushd ../mirrorgate-api
./gradlew clean build
popd

pushd ../mirrorgate-dashboard
gulp build:watch > ../tests/logs/dashboardboard.log & DASHBOARD_PID=$!
popd

pushd ../mirrorgate-backoffice
$(npm bin)/webpack --watch > ../tests/logs/backoffice.log & BACKOFFICE_PID=$!
popd

./run.sh

