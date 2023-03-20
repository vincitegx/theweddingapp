FROM eclipse-temurin:17.0.4.1_1-jre-alpine@sha256:e1506ba20f0cb2af6f23e24c7f8855b417f0b085708acd9b85344a884ba77767 AS builder
WORKDIR extracted
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.4.1_1-jre-alpine@sha256:e1506ba20f0cb2af6f23e24c7f8855b417f0b085708acd9b85344a884ba77767
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./
EXPOSE 8008
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]