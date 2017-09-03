package com.snbt.customer_mgmt.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.snbt.customer_mgmt.bl.DBService;
import com.snbt.customer_mgmt.domain.Customer;
import spark.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static com.snbt.customer_mgmt.bl.BuiltInCriteria.*;
import static spark.Spark.*;

public class CustomerController {

    public static void go(DBService dbService) {

        get("/hello", (request, response) -> "world");

        get("/customers", (request, response) -> {
            response.type("application/json");
            Predicate<Customer> criteria = resolveQueryParams(request);
            List<Customer> allCustomers = dbService.getBy(criteria);
            JsonElement allCustomersJson = new Gson().toJsonTree(allCustomers);
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, allCustomersJson));
        });

        get("/customers/:id", (request, response) -> {
            response.type("application/json");
            Optional<Customer> customersWithId = dbService.getById(request.params(":id"));
            if (customersWithId.isPresent()) {
                JsonElement customersWithIdJson = new Gson().toJsonTree(customersWithId.get());
                return new Gson()
                        .toJson(new Response(Response.Status.SUCCESS, customersWithIdJson));
            }
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, "Customer with this id was not found"));
        });

        post("/customers", (request, response) -> {
            response.type("application/json");
            Customer customer = new Gson().fromJson(request.body(), Customer.class);
            UUID uuidOfCreated = dbService.create(customer);
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS, "Created customer with Id: " + uuidOfCreated));
        });

        put("/customers/:id", (request, response) -> {
            response.type("application/json");
            Customer toUpdate = new Gson().fromJson(request.body(), Customer.class);
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

        delete("/customers/:id", (request, response) -> {
            response.type("application/json");
            dbService.delete(request.params(":id"));
            return new Gson().toJson(
                    new Response(Response.Status.SUCCESS, "Customer deleted"));
        });
    }

    private static Predicate<Customer> resolveQueryParams(Request request) {
        Predicate<Customer> criteria = everything();
        if (!request.queryParams(":firstName").isEmpty()) {
            criteria.and(firstNameIs(request.queryParams(":firstName")));
        }
        if (!request.queryParams(":city").isEmpty()) {
            criteria.and(cityIs(request.queryParams(":city")));
        }
        if (!request.queryParams(":ageFrom").isEmpty()) {
            criteria.and(ageFrom(request.queryParams(":ageFrom")));
        }
        if (!request.queryParams(":ageTo").isEmpty()) {
            criteria.and(ageTo(request.queryParams(":ageTo")));
        }
        return null;
    }
}
