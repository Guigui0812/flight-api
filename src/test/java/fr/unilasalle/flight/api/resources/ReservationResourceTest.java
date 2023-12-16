package fr.unilasalle.flight.api.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

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

    // Test de l'impossibilité de réservation d'un vol complet
    @Test
    public void createReservationUntilItIsFull() {
        int flightCapacity = 10; // Permet de boucler sur la capacité du vol

        for (int i = 0; i < flightCapacity-1; i++) {
            String passengerFirstName = "test" + i;
            String passengerSurname = "Surname" + i;
            String passengerEmail = "email" + i + "@example.com";

            String jsonBody = buildJsonBody(1, passengerFirstName, passengerSurname, passengerEmail);

            var rep = given()
                    .body(jsonBody)
                    .contentType("application/json")
                    .post("/reservations");
        }

        String passengerFirstName = "test" + flightCapacity;
        String passengerSurname = "Surname" + flightCapacity;
        String passengerEmail = "email" + flightCapacity + "@example.com";

        String jsonBody = buildJsonBody(1, passengerFirstName, passengerSurname, passengerEmail);

        given()
                .body(jsonBody)
                .contentType("application/json")
                .post("/reservations")
                .then()
                .statusCode(400)
                .body("message", equalTo("The plane is full"));
    }

    private String buildJsonBody(int passengerId, String firstName, String surname, String email) {
        return "{\n" +
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
                "    \"id\": " + passengerId + ",\n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"surname\": \"" + surname + "\",\n" +
                "    \"email\": \"" + email + "\"\n" +
                "  }\n" +
                "}";
    }
}
