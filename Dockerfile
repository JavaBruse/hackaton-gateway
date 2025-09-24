# Используем базовый образ с JDK 17
FROM eclipse-temurin:17-jre-alpine

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем .jar файл приложения в контейнер
COPY target/gateway-1.0.jar /app/gateway-1.0.jar

# Устанавливаем команду для запуска приложения
CMD ["sh", "-c", "java -jar /app/gateway-1.0.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
