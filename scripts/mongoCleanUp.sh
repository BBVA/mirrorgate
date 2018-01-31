#!/bin/bash

# Check if credentials are stored in a bucket or not
if [ -n "$SECRETS_BUCKET" ]; then
    # Load the S3 secrets file contents into the environment variables
    eval $(aws s3 cp ${SECRETS_BUCKET} - | sed 's/^/export /')
fi

IM_MASTER=`mongo --quiet --eval 'rs.isMaster().ismaster'`
if [[ $? -ne 0 ]]; then echo "Error checking MongoDB node."; exit 1; fi

if [ $IM_MASTER == "false" ];
then
    echo "Clean up can not be executed in this node (it is not a master node)."
    exit 0
fi

if [ -z ${MONGO_HOST} ]; then export MONGO_HOST="127.0.0.1"; fi
if [ -z ${MONGO_PORT} ]; then export MONGO_PORT="27017"; fi
if [ -z ${MONGO_DB} ]; then export MONGO_DB="dashboarddb"; fi

# Dump the database
echo "Cleaning up database..."
if [ -z "$SECRETS_BUCKET" ]
  then
    mongo $MONGO_HOST:$MONGO_PORT/$MONGO_DB --eval "var mongo_authdb='$MONGO_DB'" ./clean_mirrorgate_db.js
  else
    mongo $MONGO_HOST:$MONGO_PORT --eval "var mongo_host='$MONGO_HOST',mongo_port='$MONGO_PORT',mongo_user='$MONGO_USER',mongo_pass='$MONGO_PASS',mongo_authdb='$MONGO_AUTHDB'" ./clean_mirrorgate_db.js
fi
if [[ $? -ne 0 ]]; then echo "Clean up database error ($MONGO_HOST:$MONGO_PORT)."; exit 1; fi
echo "Done."
