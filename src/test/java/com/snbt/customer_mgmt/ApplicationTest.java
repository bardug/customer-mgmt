package com.snbt.customer_mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static com.snbt.customer_mgmt.ws.Web.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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

        readByCriteria(customerID);

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
        expected.setId(UUID.fromString(id));
        HttpResponse<JsonNode> jsonResponse = Unirest.get(SERVICE_URL + "/{:id}")
                .header(ACCEPT_HEADER, JSON_TYPE)
                .routeParam(":id", id)
                .asJson();

        Response readResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);
        Customer retrieved = new Gson().fromJson(readResponse.getBody(), Customer.class);

        assertThat("unsuccessful READ", readResponse.getStatus(), is(Response.Status.SUCCESS));
        assertThat("Customer read (by id) not as expected", retrieved, is(expected));
    }

    private void readByCriteria(String expectedId) throws UnirestException {
        Customer expected = new Gson().fromJson(artVandelay(), Customer.class);
        expected.setId(UUID.fromString(expectedId));
        HttpResponse<JsonNode> jsonResponse = Unirest.get(SERVICE_URL)
                .header(ACCEPT_HEADER, JSON_TYPE)
                .queryString(QueryParams.FIRST_NAME, "Art")
                .queryString(QueryParams.CITY, "Tel Aviv")
                .queryString(QueryParams.AGE_FROM, "40")
                .queryString(QueryParams.AGE_TO, "60")
                .asJson();

        Response readResponse = new Gson().fromJson(jsonResponse.getBody().toString(), Response.class);

        Type customerListType = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> retrieved = new Gson().fromJson(readResponse.getBody(), customerListType);

        assertThat("unsuccessful READ", readResponse.getStatus(), is(Response.Status.SUCCESS));
        assertThat("Customer read (by criteria) should return a single entity", retrieved, hasSize(1));
        assertThat("Customer read (by criteria) not as expected", retrieved.get(0), is(expected));
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
                "\"year\": 1970," +
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