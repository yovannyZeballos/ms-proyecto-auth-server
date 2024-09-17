FROM gradle:7.6.1-jdk17 AS builder
# Define arguments for environment variables
ARG GH_PACKAGES_USER
ARG GH_PACKAGES_TOKEN

# Set the environment variables
ENV GH_PACKAGES_USER=$GH_PACKAGES_USER
ENV GH_PACKAGES_TOKEN=$GH_PACKAGES_TOKEN

COPY build.gradle .
COPY src ./src
RUN gradle build -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /home/gradle/build/libs/ms-proyecto-auth-server-*SNAPSHOT.jar ms-proyecto-auth-server.jar
EXPOSE 9091
ENTRYPOINT ["sh", "-c", "cd /app && java -Djava.file.encoding=UTF-8 -jar ms-proyecto-auth-server.jar"]
