FROM openjdk:11
ARG JAR_FILE=server/target/*-spring-boot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=production"]