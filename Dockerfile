FROM openjdk:8-jdk-alpine
LABEL maintainer="ashokmurthy88@gmail.com"
VOLUME /tmp
ADD build/libs/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/.urandom","-jar","api-gateway.jar"]
EXPOSE 8080