# Используем образ OpenJDK
FROM eclipse-temurin:21 AS base

# Копируем jar файл приложения
COPY target/bankcards-0.0.1-SNAPSHOT.jar /app/bankcards.jar

# Переходим в рабочую директорию
WORKDIR /app

# Запускаем приложение
ENTRYPOINT ["java","-jar","bankcards.jar"]