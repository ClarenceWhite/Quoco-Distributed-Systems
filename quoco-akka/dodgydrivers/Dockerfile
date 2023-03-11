FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/dodgydrivers-0.0.1-allinone.jar /dodgydrivers.jar
CMD /usr/bin/java -jar /dodgydrivers.jar $SERVER_HOST