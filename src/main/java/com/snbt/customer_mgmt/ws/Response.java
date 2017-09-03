package com.snbt.customer_mgmt.ws;

import com.google.gson.JsonElement;

public class Response {
    private Status status;
    private String message;
    private JsonElement body;

    public Response(Status status) {
        this.status = status;
    }

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Status status, JsonElement body) {
        this.status = status;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JsonElement getBody() {
        return body;
    }

    public enum Status {
        SUCCESS ("Success"),
        FAILURE ("Failure");

        private String status;

        Status(String status) {
            this.status = status;
        }
    }
}
