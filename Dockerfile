FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM tomcat:11.0.11-jre17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /build/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
