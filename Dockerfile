FROM openjdk:16

EXPOSE 8080

ADD /build/libs/teams-services-orchestrator.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]