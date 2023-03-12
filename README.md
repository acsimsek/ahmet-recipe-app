# READ ME

## Architecture
 * This is a Spring Boot backend web application written in Java with MySql database. 
 * Database and the backend application run in separate docker containers set up with docker compose. 
 * It has an ORM layer implemented with Hibernate. 
 * Queries are implemented with Spring Data JPA and Specifications API.
 * Specifications API is chosen because there can be dynamic queries with different combinations of filters.
 * It serves the Recipes API with REST and the REST API is documented with Swagger UI.
 * Recipes and Ingredients are modelled with many to many relationship using and intermediate recipe_ingredient table.

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

### Known issues
* If you are trying to run and build on Windows, you need to replace line ending characters in "entryScript.sh" from "\r\n" to "\n"
* If start-up fails, run "docker-compose up" command again
