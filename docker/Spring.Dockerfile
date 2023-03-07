FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG SPRING_DB_PATH
ENV SPRING_DB_PATH = ${SPRING_DB_PATH}

ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=test","-jar","/app.jar"]
