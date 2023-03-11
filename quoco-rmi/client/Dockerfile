FROM openjdk:8-jre-alpine
#ENV SERVER_HOST=localhost
COPY target/client-1.0-SNAPSHOT-jar-with-dependencies.jar /client.jar
CMD /usr/bin/java -jar /client.jar $SERVER_HOST
