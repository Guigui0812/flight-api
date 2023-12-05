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
                .body("size()", equalTo(3));
    }

    @Test
    public void createReservation() {
        String jsonBody = "{\n" +
                "  \"flight\": {\n" +
                "    \"id\": 1,\n" +
                "    \"number\": \"AF101\",\n" +
                "    \"origin\": \"Paris\",\n" +
                "    \"destination\": \"New York\",\n" +
                "    \"departureDate\": \"2023-12-01\",\n" +
                "    \"departureTime\": \"07:00:00\",\n" +
                "    \"arrivalDate\": \"2023-12-01\",\n" +
                "    \"arrivalTime\": \"10:00:00\",\n" +
                "    \"plane\": {\n" +
                "      \"id\": 1,\n" +
                "      \"operator\": \"AirbusIndustrie\",\n" +
                "      \"model\": \"AIRBUS A380\",\n" +
                "      \"registration\": \"F-ABCD\",\n" +
                "      \"capacity\": 10\n" +
                "    }\n" +
                "  },\n" +
                "  \"passenger\": {\n" +
                "    \"id\": 5,\n" +
                "    \"firstName\": \"test7\",\n" +
                "    \"surname\": \"ttt\",\n" +
                "    \"email\": \"test2.ttt@gmail.com\"\n" +
                "  }\n" +
                "}";

        given()
                .body(jsonBody)
                .contentType("application/json")
                .post("/reservations")
                .then()
                .statusCode(201);
    }

    // Tester le cas où le passager n'existe pas (où il est créé automatiquement)

}
