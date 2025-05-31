FROM gradle:7.6-jdk17 AS builder
ENV OPEN_SOURCE_BUILD=true

USER root
WORKDIR /workspace/app
COPY . .
RUN ./gradlew :demo:dockerBuild

FROM openjdk:17-jre-slim
EXPOSE 8081
COPY --from=builder /workspace/app/fido2-demo/demo/build/libs/demo*.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]
