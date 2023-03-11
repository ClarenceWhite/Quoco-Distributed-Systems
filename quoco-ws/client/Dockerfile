FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/client-1.0-jar-with-dependencies.jar /client.jar
CMD /usr/bin/java -jar /client.jar $SERVER_HOST