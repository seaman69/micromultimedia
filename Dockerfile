FROM openjdk:8-jdk-alpine
MAINTAINER 3DSPACE
COPY target/micromultimedia-0.0.1-SNAPSHOT.jar micromultimedia.jar
RUN mkdir -p /root/logs/multimedia
ENTRYPOINT ["java","-jar","/micromultimedia.jar"]