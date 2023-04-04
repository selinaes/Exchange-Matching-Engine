#FROM gradle:4.7.0-jdk11-alpine AS build
FROM gradle:8.0.2-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:11-jre-slim

EXPOSE 12345

RUN mkdir /app


COPY --from=build /home/gradle/src/build/libs/*.jar /app/stock-exchange-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/stock-exchange-application.jar"]