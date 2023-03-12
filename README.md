# READ ME

## How to build and run

### Install dependencies
* Java 11
* Maven and Gradle
* Docker and Docker Compose

### How to build and run
Go to root folder of the project and run these commands in order

* "./gradlew clean build"
* "docker build -t ahmet-recipe-app:1.0 ."
* "docker-compose up"

### How to test
Check Swagger page to see the API on your browser

* http://localhost:8080/swagger-ui/#/recipe-controller

