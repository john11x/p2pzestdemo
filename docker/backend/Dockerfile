FROM eclipse-temurin:17-jre

LABEL org.opencontainers.image.source="https://github.com/john11x/p2pzestdemo"

WORKDIR /app
COPY app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]