FROM openjdk:17-jdk AS empacotamento

ARG PORT=8080

RUN mkdir /app
COPY ./build/libs/*.jar /app/app.jar
WORKDIR /app

EXPOSE ${PORT}

ENTRYPOINT exec java -jar app.jar
