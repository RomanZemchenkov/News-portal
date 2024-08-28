FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/news-portal-0.1.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
