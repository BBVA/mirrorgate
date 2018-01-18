#!/usr/bin/env bash

set -e

CUR_DIR=$(pwd)
trap 'cd $CUR_DIR' EXIT
cd "$(dirname "$0")"

pushd ../mirrorgate-dashboard

npm install
$(npm bin)/bower install
$(npm bin)/gulp dist

popd

pushd ../mirrorgate-backoffice

npm install
npm run build

popd

pushd ../mirrorgate-docs

npm install && ./node_modules/.bin/bower install --allow-root
npm run build

popd

pushd ../mirrorgate-api

./gradlew clean build

popd
