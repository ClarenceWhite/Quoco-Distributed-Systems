FROM openjdk:8-jre-alpine
ENV SERVER_HOST=activemq
COPY target/auldfellas-1.0-jar-with-dependencies.jar /auldfellas.jar
CMD /usr/bin/java -jar /auldfellas.jar $SERVER_HOST