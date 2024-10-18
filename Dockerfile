FROM openjdk:17-alpine
ADD target/order-0.0.1-SNAPSHOT.jar orderservice.jar
ENTRYPOINT ["java","-jar","/orderservice.jar"]

