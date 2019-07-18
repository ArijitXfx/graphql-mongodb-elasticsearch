FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG target/product.jar
COPY target/product.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"] 