FROM openjdk:12-alpine

RUN mkdir elenx
ADD target/monitoring-0.0.1-SNAPSHOT.jar /elenx/monitoring.jar

ENTRYPOINT ["java", "-jar", "/elenx/monitoring.jar"]