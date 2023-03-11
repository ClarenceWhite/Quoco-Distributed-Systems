FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/girlpower-0.0.1-allinone.jar /girlpower.jar
CMD /usr/bin/java -jar /girlpower.jar $SERVER_HOST