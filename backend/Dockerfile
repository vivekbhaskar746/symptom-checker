FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/symptom-checker-backend-1.0.0.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/symptom_checker
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV AZURE_AI_ENDPOINT=https://pstestopenaidply-2o475m7f43aoa.openai.azure.com/
ENV AZURE_AI_KEY=95a94311e4a84882a67dee27d87b2e80
ENV AZURE_AI_DEPLOYMENT_NAME=pstestopenaidply-2o475m7f43aoa
ENV JWT_SECRET=myVeryLongSecretKeyThatIsAtLeast256BitsLongForJWTSecurity1234567890

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
