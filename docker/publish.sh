#!/usr/bin/env bash

set -e
set -x

echo "Publishing image in Docker Hub"

if [ $# -eq 0 ]
  then
    echo "No tag image spcified"
    exit 1;
fi

docker login --username $DOCKER_USER --password $DOCKER_PASSWORD
docker tag mirrorgate bbvaae/mirrorgate:$1
docker push bbvaae/mirrorgate:$1
