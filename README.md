# simple-crud
A simple **C**reate **R**ead **U**pdate **D**elete application using Akka-Http, Slick and PostgreSQL.

## Prerequisites
- Java 8, 12
- sbt
- docker and docker-compose (**[OPTIONAL]** for database setup)

## Getting Started
Before running the service, ensure that the database is running:

### Database Setup
#### Environment Variable
This is used to provide the details that are confidential to the application. Create a duplicate of the `.env.example` file and rename it as `.env`. Ensure to enter the required field as provided in the example.

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
Check the `docker-compose.yml` file for the docker dependencies. The fields can be changed as required.

Adminer provides a user interface that can be used to manage the database. Run the host url(e.g localhost) on the port specified (e.g 8080).

### Run application
#### Using SBT
Use the `sbt run` command

#### Unit-Tests
Run the sbt command to run the test cases
````bash
sbt test
````

#### API Documentation using Swagger/OpenAPI
The API is documented using the OpenAPI 3.0 specification. The JSON format can be found in the - `/api-docs/swagger.json` path. You can also test the endpoints using the Swagger tool by calling either the `/swagger` or `/index.html` route.
