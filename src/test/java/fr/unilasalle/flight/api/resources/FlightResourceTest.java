package fr.unilasalle.flight.api.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class FlightResourceTest {

    @Test
    public void getAllFlights() {
        given()
                .when().get("/flights")
                .then()
                .statusCode(200);
    }

    @Test
    public void getFlightById() {
        given()
                .when().get("/flights/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    public void getFlightByDestination() {
        given()
                .when().get("/flights?destination=New York")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    public void getFlightByDestination404() {
        given()
                .when().get("/flights?destination=Abbeville")
                .then()
                .statusCode(404);
    }

    @Test
    public void createFlight() {
        given()
                .when()
                .body("{\n" +
                        "    \"number\": \"AF123\",\n" +
                        "    \"origin\": \"Paris\",\n" +
                        "    \"destination\": \"New York\",\n" +
                        "    \"departureDate\": \"2021-01-01\",\n" +
                        "    \"departureTime\": \"10:00\",\n" +
                        "    \"arrivalDate\": \"2021-01-01\",\n" +
                        "    \"arrivalTime\": \"12:00\",\n" +
                        "    \"plane\": {\n" +
                        "        \"id\": 1\n" +
                        "    }\n" +
                        "}")
                .contentType("application/json")
                .post("/flights")
                .then()
                .statusCode(201);
    }

    @Test
    public void deleteFlight() {
        given()
                .when().delete("/flights/2")
                .then()
                .statusCode(201);
    }
}
