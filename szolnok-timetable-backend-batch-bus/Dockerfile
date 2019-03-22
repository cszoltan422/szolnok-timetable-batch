
FROM openjdk:8-jre

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/apps/szolnok-timetable-backend-batch-bus.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/apps/szolnok-timetable-backend-batch-bus.jar