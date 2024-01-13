FROM eclipse-temurin:19-jre-focal
# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл внутрь контейнера
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service.jar

# Определяем команду для запуска приложения при старте контейнера
CMD ["java", "-jar", "user-service.jar"]
