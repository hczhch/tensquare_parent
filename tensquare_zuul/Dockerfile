#FROM java:8
FROM openjdk:8-jdk-alpine
COPY target/app.jar app.jar
EXPOSE 10020
ENTRYPOINT ["java","-jar","/app.jar"]