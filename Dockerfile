FROM openjdk:8
VOLUME /tmp
EXPOSE 8001
ADD ./target/MELIRestApi-1.0.jar MELIRestApi.jar
ENTRYPOINT ["java", "-jar", "/MELIRestApi.jar"]