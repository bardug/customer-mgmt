package com.snbt.customer_mgmt;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.snbt.customer_mgmt.domain.Customer;
import com.snbt.customer_mgmt.ws.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.snbt.customer_mgmt.ws.Web.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class ApplicationTest {

    @BeforeAll
    static void setUp() {
        Main.Application.start();
    }

    @Test
    @DisplayName("Complete flow of create, read, update and delete")
    void CRUD() throws Exception {
        String customerID = create();

        read(customerID);

        update(customerID);

        delete(customerID);
    }

    private String create() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post(SERVICE_URL)
                .header(ACCEPT_HEADER, JSON_TYPE)
                .body(artVandelay())
                .asJson();
        Response createResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);
        UUID uid = new Gson().fromJson(createResponse.getBody(), UUID.class);

        assertThat("unsuccessful CREATE", createResponse.getStatus(), is(Response.Status.SUCCESS));

        return uid.toString();
    }

    private void read(String id) throws UnirestException {
        Customer expected = new Gson().fromJson(artVandelay(), Customer.class);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(SERVICE_URL + "/{:id}")
                .header(ACCEPT_HEADER, JSON_TYPE)
                .routeParam(":id", id)
                .asJson();

        Response readResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);
        Customer retrieved = new Gson().fromJson(readResponse.getBody(), Customer.class);

        assertThat("unsuccessful READ", readResponse.getStatus(), is(Response.Status.SUCCESS));
        assertThat("Customer read not as expected", retrieved, is(expected));
    }

    private void update(String id) throws UnirestException {
        Customer toUpdate = new Gson().fromJson(artVandelay(), Customer.class);
        toUpdate.setFirstName("Kal");
        toUpdate.setLastName("Varnsen");
        HttpResponse<JsonNode> jsonResponse = Unirest.put(SERVICE_URL + "/{:id}")
                .header(ACCEPT_HEADER, JSON_TYPE)
                .routeParam(":id", id)
                .body(new Gson().toJson(toUpdate))
                .asJson();

        Response updateResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);

        assertThat("unsuccessful UPDATE", updateResponse.getStatus(), is(Response.Status.SUCCESS));
    }

    private void delete(String id) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.delete(SERVICE_URL + "/{:id}")
                .header(ACCEPT_HEADER, JSON_TYPE)
                .routeParam(":id", id)
                .asJson();
        Response deleteResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);
        assertThat("unsuccessful DELETE", deleteResponse.getStatus(), is(Response.Status.SUCCESS));
    }

    @AfterAll
    static void tearDown() {
        Main.Application.stop();
    }

    private static String artVandelay() {
        return "{" +
                "\"id\": \"5c17a5d8-5e90-4089-adf4-2bf5dfb35095\"," +
                "\"firstName\": \"Art\"," +
                "\"lastName\": \"Vandelay\"," +
                "\"address\": {" +
                "\"streetName\": \"Dafna\"," +
                "\"houseNum\": 43," +
                "\"city\": \"Tel Aviv\"," +
                "\"zip\": 44234" +
                "}," +
                "\"contactInformation\": {" +
                "\"emailAddress\": \"artv@vandelay-industries.com\"," +
                "\"phoneNumber\": \"054-2345678\"" +
                "}," +
                "\"dateOfBirth\": {" +
                "\"year\": 1980," +
                "\"month\": 3," +
                "\"day\": 4" +
                "}," +
                "\"creditCard\": {" +
                "\"number\": \"1234-5678-1111-2222\"," +
                "\"expiry\": {" +
                "\"year\": 2020," +
                "\"month\": 9," +
                "\"day\": 3" +
                "}," +
                "\"cvv\": 888" +
                "}," +
                "\"drivingLicenseNumber\": 567890009" +
                "}";
    }
}