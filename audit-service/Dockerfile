FROM eclipse-temurin:19-jre-focal
MAINTAINER DMITRY
# Устанавливаем рабочую директорию
WORKDIR /app

COPY target/audit-service-0.0.1-SNAPSHOT.jar audit-service.jar

CMD ["java", "-jar", "audit-service.jar"]
