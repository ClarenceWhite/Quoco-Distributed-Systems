FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/auldfellas-0.0.1-allinone.jar /auldfellas.jar
CMD /usr/bin/java -jar /auldfellas.jar $SERVER_HOST