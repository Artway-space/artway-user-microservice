FROM arm32v7/openjdk
ADD target/artway-user.jar artway-user.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/artway-user.jar"]