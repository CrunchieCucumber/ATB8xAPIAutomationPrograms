package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class APITest002_Integration1 {

    RequestSpecification requestSpecification;
    Response response;
    ValidatableResponse validatableResponse;

    String token;
    String bookingId;

    public String getToken() {
        //given
        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/auth");
        requestSpecification.contentType(ContentType.JSON).log().all();
        requestSpecification.body(payload);

        //when
        response = requestSpecification.when().post();

        //then
        validatableResponse = response.then();
        validatableResponse.statusCode(200);

        //extract the token
        token = response.jsonPath().getString("token");
        System.out.println(token);
        return token;
    }

    public String getBookingId() {
        //given
        String payload_POST = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking");
        requestSpecification.contentType(ContentType.JSON).log().all();
        requestSpecification.body(payload_POST);

        //when
        response = requestSpecification.when().post();

        //then
        validatableResponse = response.then();
        validatableResponse.statusCode(200);

        //extract booking ID
        bookingId = response.jsonPath().getString("bookingid");
        System.out.println(bookingId);
        return bookingId;
    }

    @Test(priority = 1)
    public void test_update_request_put() {
        String token = getToken();
        System.out.println(token);
        String bookingId = getBookingId();
        System.out.println(bookingId);

        //Now that we have all details, create put request to update details

        //given
        String payload_PUT = "{\n" +
                "    \"firstname\" : \"Ayushi\",\n" +
                "    \"lastname\" : \"Shukla\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.contentType(ContentType.JSON).log().all();
        requestSpecification.cookie("token",token);
        requestSpecification.body(payload_PUT);

        //when
        response = requestSpecification.when().put();

        //then
        validatableResponse = response.then();
        validatableResponse.statusCode(200);
    }

    @Test(priority = 2)
    public void test_update_request_get() {
        System.out.println(bookingId);

        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.when().get();
        requestSpecification.then().log().all().statusCode(200);
    }

    @Test(priority = 3)
    public void test_delete_booking() {
        System.out.println(bookingId);
        System.out.println(token);

        //given
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.contentType(ContentType.JSON).log().all();
        requestSpecification.cookie("token",token);

        //when
        response = requestSpecification.when().delete();

        //then
        validatableResponse = response.then();
        validatableResponse.statusCode(201);

    }

    @Test(priority = 4)
    public void test_after_delete_request_get() {
        System.out.println(bookingId);

        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.when().get();
        requestSpecification.then().log().all().statusCode(404); // #TODO
    }
}
