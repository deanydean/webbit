#
# Dockerfile to build and run webbit container
#

# Stage 1: builds the webbit.jar
FROM gradle:jdk11 as build
ADD build.gradle build.gradle
ADD src src
RUN gradle --no-daemon build

# Stage 2: runs webbit
FROM openjdk:11-jre-slim
WORKDIR /webbit
ADD etc/default-index.html /webbit/www/index.html
ADD etc/default-webbit.json /webbit/webbit.json
COPY --from=build /home/gradle/build/libs/webbit.jar webbit.jar

ENV WEBBIT_HOME "/webbit"
EXPOSE 80

ENTRYPOINT [ "java", "-jar", "webbit.jar" ]