FROM openjdk:17-alpine

RUN addgroup -S app && adduser -S app -G app

USER app

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
