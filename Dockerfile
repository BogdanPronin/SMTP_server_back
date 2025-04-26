# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Gradle-файлы
COPY build.gradle.kts settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN ./gradlew build -x test

# Указываем порт
EXPOSE 8081

# Запускаем приложение
CMD ["java", "-jar", "build/libs/smtpauth-0.0.1-SNAPSHOT.jar"]
