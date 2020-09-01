FROM openjdk:14.0.2-jdk

# Install AWS CLI 2
RUN microdnf -y install curl unzip \
    && cd /tmp \
    && curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install -i /usr/local -b /usr/local/bin -u \
    && rm  -rf awscliv2.zip awscliv2 \
    && aws --version

# Copy the entry-point script
COPY entrypoint.sh /mirrorgate/entrypoint.sh

COPY temp /mirrorgate
ENV JAVA_OPTS=""
WORKDIR /mirrorgate

ENTRYPOINT ["/mirrorgate/entrypoint.sh"]
