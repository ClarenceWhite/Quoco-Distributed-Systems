FROM openjdk:8-jre-alpine
ENV SERVER_HOST=dodgydrivers
COPY target/dodgydrivers-1.0-jar-with-dependencies.jar /dodgydrivers.jar
CMD /usr/bin/java -jar /dodgydrivers.jar $SERVER_HOST