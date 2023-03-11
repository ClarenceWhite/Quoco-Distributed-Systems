FROM openjdk:8-jre-alpine
ENV SERVER_HOST=activemq
COPY target/girlpower-1.0-jar-with-dependencies.jar /girlpower.jar
CMD /usr/bin/java -jar /girlpower.jar $SERVER_HOST