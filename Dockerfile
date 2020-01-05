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
ADD etc/default-application.yaml /webbit/webbit.yaml
COPY --from=build /home/gradle/build/libs/webbit.jar webbit.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "webbit.jar" ]