#!/usr/bin/env bash

set -e

trap 'docker-compose -p ${BUILD_TAG} down --volumes' EXIT

if [ -z ${BUILD_TAG+x} ]; then export BUILD_TAG=tests; fi

docker-compose -p ${BUILD_TAG} run -u $(id -u) mongo-populate
docker-compose -p ${BUILD_TAG} run -u $(id -u) --service-ports app
