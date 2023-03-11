FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/broker-0.0.1-allinone.jar /broker.jar
CMD /usr/bin/java -jar /broker.jar $SERVER_HOST