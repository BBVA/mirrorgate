#!/usr/bin/env bash

set -e

volume_token='{{ volume }}'
volume=".:/temp"

entrypoint_token='{{ entrypoint }}'
entrypoint="/usr/bin/env bash -c 'until mongo dashboarddb --host mongo < data/main.js; do >&2 echo Mongo is unavailable - sleeping & sleep 1; done'"

trap 'docker-compose -p ${BUILD_TAG} down --volumes; rm -f docker-compose.yml' EXIT

if [ -z ${BUILD_TAG+x} ]; then export BUILD_TAG=tests; fi

if [ -n "$MONGODUMP_BUCKET" ];
then
    dumps=(`aws s3 --profile $AWS_PROFILE --region $AWS_REGION ls $MONGODUMP_BUCKET/ | awk '{print $4}'`)
    last_dump=${dumps[-1]}
    aws s3 --profile $AWS_PROFILE --region $AWS_REGION cp $MONGODUMP_BUCKET/${last_dump} /tmp
    tar -xf /tmp/${last_dump} --directory /tmp

    entrypoint="/usr/bin/env bash -c 'until mongorestore --host mongo --db dashboarddb --drop /temp/dashboarddb; do >&2 echo Mongo is unavailable - sleeping & sleep 1; done'"
    volume="/tmp/${last_dump%%.*}:/temp"

    awk -v old="${volume_token}" -v new="${volume}" 's=index($0,old){$0=substr($0,1,s-1) new substr($0,s+length(old))} 1' < docker-compose-template.yml > docker-compose-template.tmp
    awk -v old="${entrypoint_token}" -v new="${entrypoint}" 's=index($0,old){$0=substr($0,1,s-1) new substr($0,s+length(old))} 1' < docker-compose-template.tmp > docker-compose.yml && rm docker-compose-template.tmp
fi

awk -v old="${volume_token}" -v new="${volume}" 's=index($0,old){$0=substr($0,1,s-1) new substr($0,s+length(old))} 1' < docker-compose-template.yml > docker-compose-template.tmp
awk -v old="${entrypoint_token}" -v new="${entrypoint}" 's=index($0,old){$0=substr($0,1,s-1) new substr($0,s+length(old))} 1' < docker-compose-template.tmp > docker-compose.yml && rm docker-compose-template.tmp

docker-compose -p ${BUILD_TAG} run -u $(id -u) mongo-populate
docker-compose -p ${BUILD_TAG} run -u $(id -u) --service-ports app
