FROM gradle:7.6-jdk17 AS builder
ENV OPEN_SOURCE_BUILD=true

USER root
WORKDIR /workspace/app
COPY . .
RUN ./gradlew :rpserver:dockerBuild

FROM openjdk:17-jre-slim
EXPOSE 8080
COPY --from=builder /workspace/app/rpserver/build/libs/rpserver*.jar rpserver.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/rpserver.jar"]
