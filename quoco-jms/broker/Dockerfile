FROM openjdk:8-jre-alpine
ENV SERVER_HOST=activemq
COPY target/broker-1.0-jar-with-dependencies.jar /broker.jar
CMD /usr/bin/java -jar /broker.jar $SERVER_HOST