package fr.unilasalle.flight.api.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PassengerResourcesTest {

    @Test
    public void getAllPassengers() {
        given()
                .when().get("/passengers")
                .then()
                .statusCode(200);
    }

    @Test
    public void getPassengerByEmailAddress() {
        given()
                .when().get("/passengers?email=guillaume.rohee@gmail.com")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    public void updatePassenger() {
        given()
                .when()
                .body("{\n" +
                        "    \"id\": 4,\n" +
                        "    \"firstName\": \"Ines\",\n" +
                        "    \"surname\": \"ROHEE\",\n" +
                        "    \"email\": \"ines.delmerle@gmail.com\"\n" +
                        "}")
                .contentType("application/json")
                .put("/passengers")
                .then()
                .statusCode(201)
                .body(equalTo("Passenger updated"));
    }

    // Check an incorrect email address
    @Test
    public void CreatePassengerIncorrectEmailAddress() {
        given()
                .when()
                .body("{\n" +
                        "    \"firstName\": \"Ines\",\n" +
                        "    \"surname\": \"ROHEE\",\n" +
                        "    \"email\": \"ines.delmerle\"\n" +
                        "}")
                .contentType("application/json")
                .post("/passengers")
                .then()
                .statusCode(405);
    }
}