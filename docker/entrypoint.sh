#!/bin/bash

# Check if credentials are stored in a bucket or not
if [ -z "$SECRETS_BUCKET" ]; then
    java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $(ls *.jar)
else
    # Load the S3 secrets file contents into the environment variables
    eval $(aws s3 cp ${SECRETS_BUCKET} - | sed 's/^/export /')
    java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $(ls *.jar)
fi