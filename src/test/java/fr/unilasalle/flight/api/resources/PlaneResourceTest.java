package fr.unilasalle.flight.api.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PlaneResourceTest {

    @Test
    public void getAllPlanes() {
        given()
                .when().get("/planes")
                .then()
                .statusCode(200);
    }

    @Test
    public void getPlanesByOperator() {
        given()
                .when().get("/planes?operator=AirbusIndustrie")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    public void getPlaneById() {
        given()
                .when().get("/planes/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    public void createPlane() {
        given()
                .when()
                .body("{\n" +
                        "    \"model\": \"A380\",\n" +
                        "    \"capacity\": 10,\n" +
                        "    \"operator\": \"SpaceX\",\n" +
                        "    \"registration\": \"TA876\"\n" +
                        "}")
                .contentType("application/json")
                .post("/planes")
                .then()
                .statusCode(201)
                .body(equalTo("Plane created"));
    }
}