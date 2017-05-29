FROM openjdk:8-jdk

COPY temp /mirrorgate
ENV JAVA_OPTS=""
WORKDIR /mirrorgate

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $(ls *.jar)" ]
