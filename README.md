# Async-Data-Aggregator

## Introduction

This Service is designed to retrieve the User Information for a given userId, aggregates everything together before
returning it to the consumer. For this the Service relies upon retrieving Data from these two endpoints and merges
them together:

- https://jsonplaceholder.typicode.com/users/{userId}
- https://jsonplaceholder.typicode.com/posts?userId={userId}

To be as lightweight as possible this Service utilises Ktor and Coroutines, to handle non-blocking operations like 
HTTP/S calls, while also allowing us to do these in Parallel, while not using any form of persistence/caching.

## Usage

### Running the Service

To be able to run the Service and then later on use it, there are two Options either running it with Gradle on the local PC or by running it 
in a Docker container.

#### Gradle

To be able to run it in Gradle either run it directly with gradle by running `./gradlew run` or run it in Intellij by
using the provided Intellij RunConfiguration `ApplicationKt`. 

The tests for this Service can be run by either executing `./gradlew test` (for only the tests) or `./gradlew build`
(for running the tests and for building the jar).

#### Docker Container

1. To be able to run it in Docker, the jar first needs to be built, this can be done either with either of these two:
    - `./gradlew buildFatJar` (to only build the jar and nothing else)
    - `./gradlew build` (to build the jar and also run all tests)

2. Then to run this Jar in a docker container we need to build an image with the provided Dockerfile.
   ```shell 
   docker build . -t async-data-aggregator
   ```
   
    and then run the Image in a Container, this also exposes the Port 8080 of the container to the host. 

    ```shell
    docker run -p 8080:8080 async-data-aggregator
    ```
   
   There is also an Intellij Run Configuration for this Dockerfile provided, that does the building & running for you
   (it also exposes the Port 8080 to the host).

#### Using the Service

Once the Service is running, it exposes the `/users/{userId}` Endpoint on Port 8080. Where the userId is an Integer
value representing the userId of the user for which the user information should get retrieved.

An example could look like this:

   ```shell 
   curl  http://localhost:8080/users/1
   ```

The Service may respond with the following status codes:
- 200: If the Request was successful
- 400: If the userId Parameter was malformed
- 404: If either the requested user does not exist or the Request was malformed and tried to call a non-existent Endpoint
- 500: If an unexpected technical error occurred