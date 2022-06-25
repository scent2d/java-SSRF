FROM openjdk:11 as intermediate
RUN apt-get update
RUN apt-get install -y maven
WORKDIR /ssrf-java
COPY src /ssrf-java/src
COPY pom.xml /ssrf-java/pom.xml
COPY application.server.properties /ssrf-java/application.properties
RUN mvn package
ENTRYPOINT ["java","-jar","target/ssrf-java.jar"]