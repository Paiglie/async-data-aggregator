FROM amazoncorretto:21 AS runtime
EXPOSE 8080
RUN mkdir /app
COPY build/libs/*.jar /app/async-data-aggregator-all.jar
ENTRYPOINT ["java","-jar","/app/async-data-aggregator-all.jar"]
