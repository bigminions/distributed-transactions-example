FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD demo.jar demo.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/demo.jar"]