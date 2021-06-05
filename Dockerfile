FROM arm32v7/openjdk
ADD target/artway-user-ms.jar artway-user-ms.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/artway-user-ms.jar"]