FROM gradle:7.6-jdk17 AS builder
ENV OPEN_SOURCE_BUILD=true

USER root
WORKDIR /workspace/app
COPY . .
RUN ./gradlew :fido2-core:dockerBuild

FROM eclipse-temurin:17-jre
EXPOSE 8081
COPY --from=builder /workspace/app/fido2-core/build/libs/fido2-core*.jar fido2-core.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","/fido2-core.jar"]
