FROM amazoncorretto:11-alpine-jdk
MAINTAINER onbe.com
EXPOSE 9200
COPY mssql.jks /tmp/mssql.jks
COPY target/order-service-exec.jar order-service-exec.jar
ENTRYPOINT ["java","-jar","/order-service-exec.jar", "-p", "9200"]
