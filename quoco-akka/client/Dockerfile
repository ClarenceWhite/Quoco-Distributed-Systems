FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/client-0.0.1-allinone.jar /client.jar
CMD /usr/bin/java -jar /client.jar $SERVER_HOST