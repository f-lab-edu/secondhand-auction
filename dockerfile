FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "-Dspring-boot.run.arguments--spring.datasource.url=${DB_URL}, --spring.datasource.driver-class-name=${DB_CLASS_NAME}, --spring.datasource.username=${DB_USERNAME}"]
