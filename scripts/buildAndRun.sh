#!/usr/bin/env bash

set -e

CUR_DIR=$(pwd)
trap 'cd $CUR_DIR' EXIT
cd "$(dirname "$0")"

./buildAll.sh

pushd ../tests
./run.sh
popd
