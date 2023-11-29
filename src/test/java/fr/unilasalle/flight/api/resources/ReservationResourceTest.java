package fr.unilasalle.flight.api.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ReservationResourceTest {

    @Test
    public void getAllReservations() {
        given()
                .when().get("/reservations")
                .then()
                .statusCode(200);
    }

    @Test
    public void getReservationByFlightNumber() {
        given()
                .when().get("/reservations?flightNumber=AF101")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void createReservation() {
        given()
                .when()
                .body("{\n" +
                        "    \"flightNumber\": \"AF124\",\n" +
                        "    \"passenger\": {\n" +
                        "        \"id\": 1\n" +
                        "    }\n" +
                        "}")
                .contentType("application/json")
                .post("/reservations")
                .then()
                .statusCode(201)
                .body(equalTo("Reservation created"));
    }

    // Tester le cas où le passager n'existe pas (où il est créé automatiquement)

}
