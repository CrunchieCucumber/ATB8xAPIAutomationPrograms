package org.example;
import io.restassured.RestAssured;

public class APITest001 {
    public static void main(String[] args) {
        //System.out.println("API testing");

        //Gherkin Syntax
        RestAssured
                .given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking/1")
                .when()
                    .get()
                .then().log().all()
                    .statusCode(200); //for positive tc, will not give anything in console
                  //.statusCode(201); //for negative tc, will give error in console
    }
}
