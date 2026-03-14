FROM --platform=linux/arm64 eclipse-temurin:25-jre-alpine
LABEL authors="Ben Shabowski"

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

WORKDIR /app
COPY /target/thaye-1.0.0.jar /app/thaye-1.0.0.jar

EXPOSE 8080

CMD ["java", "-jar", "thaye-1.0.0.jar"]
