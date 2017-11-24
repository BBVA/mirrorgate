#!/usr/bin/env bash

set -e

trap 'docker-compose -p ${BUILD_TAG} down --volumes;' EXIT

if [ -z ${BUILD_TAG+x} ]; then export BUILD_TAG=tests; fi

if [ -n "$MONGODUMP_BUCKET" ];
then
    dumps=(`aws s3 --profile $AWS_PROFILE --region $AWS_REGION ls $MONGODUMP_BUCKET/ | awk '{print $4}'`)
    last_dump=${dumps[-1]}
    aws s3 --profile $AWS_PROFILE --region $AWS_REGION cp $MONGODUMP_BUCKET/${last_dump} /tmp
    tar -xf /tmp/${last_dump} --directory /tmp

    rm -f /tmp/latest_mongo_dump || true
    ln -s /tmp/${last_dump%%.*} /tmp/latest_mongo_dump
    docker-compose -p ${BUILD_TAG} run -u $(id -u) mongo-populate-dump
else
    docker-compose -p ${BUILD_TAG} run -u $(id -u) mongo-populate-test
fi

docker-compose -p ${BUILD_TAG} run -u $(id -u) --service-ports app
