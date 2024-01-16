#!/bin/sh
java -jar /app.jar --spring.datasource.url=$DB_URL --spring.datasource.driver-class-name=$DB_CLASS_NAME --spring.datasource.username=$DB_USERNAME