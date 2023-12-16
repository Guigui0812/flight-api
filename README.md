# flight-api

This project uses Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

It has been developp during my last year of studies at ESIEE Amiens in the context of a Java EE course dispensed by Meg-Ann CAMUS.

## Description

This project is a REST API that allows you to manage flights and passengers. 

The following operations are available :
- Create a plane
- Get all planes according to different criteria
- Create a flight that is linked to a plane
- Get flights according to different criteria
- Reserve a seat on a flight (if there are still seats available)
- Cancel a Flight and delete all reservations associated with it
- Create a passenger if a reservation is made for a passenger who does not exist
- Get passengers according to different criteria
- Update a passenger
- Delete a passenger

It is based on a relational in-memory database (H2) and uses Hibernate ORM to manage the persistence of the data.

The framework used to the HTTP part is RESTEasy. 

### Swagger

This API is accessible via a Swagger UI at http://localhost:8080/q/swagger-ui/#/ when the project is running.

On this page you can test the different endpoints of the API. 

### Tests

This project contains unit tests and integration tests. 

To run the tests : 
```shell script
mvn test
```

### Database

If you want to manage the database, you can access the H2 console at http://localhost:8080/h2 when the project is running.

They've been written with RestAssured and allow to test the different endpoints of the API according to different scenarios.

## Running the application in dev mode

This app can be run in dev mode : 
```shell script
mvn quarkus:dev
```