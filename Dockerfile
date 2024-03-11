FROM openjdk:21
ARG JAR_FILE=main/build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
