# simple-crud
A simple **C**reate **R**ead **U**pdate **D**elete application using Akka-Http, Slick and PostgreSQL.

## Prerequisites
- Java 12
- sbt
- docker and docker-compose (**[OPTIONAL]** for database setup)

## Getting Started
Before running the service, ensure that the database is running:

### Database Setup
#### Using docker
For database setup using *docker*, run the scripts below;
- to download a [Postgres](http://www.postgresql.org/) and Adminer image (if they do not exist) and start the containers.
````bash
sh start.sh
````
- to stop and delete the containers. **This should be used with care, as it will erase every stored data**.
````bash
sh remove.sh
````
Check the `stack.yml` file for the docker dependencies. The fields can be changed as required.

Adminer provides a user interface that can be used to manage the database. Run the host url(e.g localhost) on the port specified (e.g 8080).

### Run application
#### Using SBT
Use the `sbt run` command

## TODO
#### Unit-Tests
Run the sbt command to run the test cases
````bash
sbt test
````

#### API Documentation using Swagger or OpenAPI

#### Deploy on AWS or Heroku
