FROM openjdk:8-jdk

# Install the AWS CLI
RUN apt-get update && apt-get -y install python curl unzip && cd /tmp && curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip" && unzip awscli-bundle.zip && ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws && rm awscli-bundle.zip && rm -rf awscli-bundle

# Copy the entry-point script
COPY entrypoint.sh /mirrorgate/entrypoint.sh

COPY temp /mirrorgate
ENV JAVA_OPTS=""
WORKDIR /mirrorgate

ENTRYPOINT ["/mirrorgate/entrypoint.sh"]