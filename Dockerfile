FROM openjdk:16

EXPOSE 8080

ADD /build/libs/teams-service-unspecified.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]