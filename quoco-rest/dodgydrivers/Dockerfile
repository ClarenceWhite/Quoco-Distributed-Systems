FROM openjdk:8-jre-alpine
ENV SERVER_HOST=dodgydrivers
COPY target/dodgydrivers-0.0.1-SNAPSHOT.jar /dodgydrivers.jar
CMD /usr/bin/java -jar /dodgydrivers.jar $SERVER_HOST