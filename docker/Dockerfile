FROM openjdk:12-jdk

# Copy the entry-point script
COPY entrypoint.sh /mirrorgate/entrypoint.sh

COPY temp /mirrorgate
ENV JAVA_OPTS=""
WORKDIR /mirrorgate

ENTRYPOINT ["/mirrorgate/entrypoint.sh"]