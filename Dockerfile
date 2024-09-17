FROM gradle:7.6.1-jdk17 AS builder
COPY build.gradle .
COPY src ./src
RUN gradle build -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /home/gradle/build/libs/ms-proyecto-auth-server-*SNAPSHOT.jar ms-proyecto-auth-server.jar
EXPOSE 9091
ENTRYPOINT ["sh", "-c", "cd /app && java -Djava.file.encoding=UTF-8 -jar ms-proyecto-auth-server.jar"]