FROM eclipse-temurin:17-jdk AS build

WORKDIR /opt/app

ARG JAR_FILE=build/libs/savemyreceipt_v2-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]

EXPOSE 8080