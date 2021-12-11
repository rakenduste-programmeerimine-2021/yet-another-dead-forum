FROM maven:3.8.2-openjdk-11

COPY . /backend

WORKDIR /backend

RUN mvn clean install -DskipTests

EXPOSE 8080