package com.snbt.customer_mgmt.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.snbt.customer_mgmt.bl.DBService;
import com.snbt.customer_mgmt.bl.InMemoryCriteria;
import com.snbt.customer_mgmt.domain.Customer;
import spark.Request;
import spark.Spark;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static com.snbt.customer_mgmt.bl.InMemoryCriteria.Predicates.*;
import static com.snbt.customer_mgmt.ws.Web.JSON_TYPE;
import static com.snbt.customer_mgmt.ws.Web.SERVICE_PATH;
import static spark.Spark.*;

public class CustomerController {

    public static void go(DBService<Customer, InMemoryCriteria> dbService) {

        get(SERVICE_PATH, (request, response) -> {
            response.type(JSON_TYPE);
            InMemoryCriteria criteria = resolveQueryParams(request);
            List<Customer> fetchedCustomers = dbService.getBy(criteria);
            JsonElement fetchedCustomersJson = new Gson().toJsonTree(fetchedCustomers);
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, fetchedCustomersJson));
        });

        get(SERVICE_PATH + "/:id", (request, response) -> {
            response.type(JSON_TYPE);
            Optional<Customer> customersWithId = dbService.getById(request.params(":id"));
            if (customersWithId.isPresent()) {
                JsonElement customersWithIdJson = new Gson().toJsonTree(customersWithId.get());
                return new Gson()
                        .toJson(new Response(Response.Status.SUCCESS, customersWithIdJson));
            }
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, "Customer with this id was not found"));
        });

        post(SERVICE_PATH, (request, response) -> {
            response.type(JSON_TYPE);
            Customer customer = new Gson().fromJson(request.body(), Customer.class);
            UUID uuidOfCreated = dbService.create(customer);
            JsonElement uuidJson = new Gson().toJsonTree(uuidOfCreated);
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, uuidJson));
        });

        put(SERVICE_PATH + "/:id", (request, response) -> {
            response.type(JSON_TYPE);
            Customer toUpdate = new Gson().fromJson(request.body(), Customer.class);
            toUpdate.setId(UUID.fromString(request.params(":id")));
            Optional<Customer> updatedCustomer = dbService.update(toUpdate);

            if (updatedCustomer.isPresent()) {
                return new Gson().toJson(
                        new Response(Response.Status.SUCCESS, new Gson()
                                .toJsonTree(updatedCustomer)));
            }
            return new Gson().toJson(
                    new Response(Response.Status.FAILURE, new Gson()
                            .toJson("Customer with this id was not found")));
        });

        delete(SERVICE_PATH + "/:id", (request, response) -> {
            response.type(JSON_TYPE);
            dbService.delete(request.params(":id"));
            return new Gson().toJson(
                    new Response(Response.Status.SUCCESS, "Customer deleted"));
        });
    }

    private static InMemoryCriteria resolveQueryParams(Request request) {
        Predicate<Customer> predicate = everything();
        if (request.queryMap().hasKeys()) {
            for (String param : request.queryParams()) {
                switch (param) {
                    case Web.QueryParams.FIRST_NAME:
                        predicate = predicate.and(firstNameIs(request.queryParams(Web.QueryParams.FIRST_NAME)));
                        break;
                    case Web.QueryParams.CITY:
                        predicate = predicate.and(cityIs(request.queryParams(Web.QueryParams.CITY)));
                        break;
                    case Web.QueryParams.AGE_FROM:
                        predicate = predicate.and(ageFrom(request.queryParams(Web.QueryParams.AGE_FROM)));
                        break;
                    case Web.QueryParams.AGE_TO:
                        predicate = predicate.and(ageTo(request.queryParams(Web.QueryParams.AGE_TO)));
                }
            }
        }

        return InMemoryCriteria.from(predicate);
    }

    public static void stop() {
        Spark.stop();
    }
}
