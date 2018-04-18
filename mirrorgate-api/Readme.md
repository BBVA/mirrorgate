# MirrorGate API

Spring application that serves as backend for the MirrorGate application. The API is used by the dashboard module to retrieve the information to be displayed.

This project uses Spring Boot to package the collector as an executable JAR with dependencies.

## Building and running the module
If you want to run the tests, use
```
gradle test
```

To package the collector into an executable JAR file, run:
```bash
gradle build
```
The resultant fat jar can be found in
```
/build/libs
```
Copy this file to your server and launch it using:
```bash
java -jar mirrorgate-api-<version>.jar
```
If you want to change the database used, launch the fat jar with the following command:
```
java -Dspring.data.mongodb.uri="mongodb://localhost/dashboarddb" -jar mirrorgate-api-0.0.1-SNAPSHOT.jar
```

## Profiles
There are two different profiles defined in the application
- embedded: No security and local mongo instance. Configuration can be found on **application-embedded.properties**
- default: header based security and mongo connection specified in **application.properties** file

Active profiles can be changed using the property ***spring.profiles.active***

## Generate API documentation
You can generate Open API definition documentation running the SwaggerToMarkup test