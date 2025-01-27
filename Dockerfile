FROM maven:3.9.9-amazoncorretto-23-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:23-alpine
COPY --from=build /target/*.jar rpg-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/rpg-backend.jar"]