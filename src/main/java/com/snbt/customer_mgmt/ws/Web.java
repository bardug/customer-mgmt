package com.snbt.customer_mgmt.ws;

public final class Web {

    private Web() {
    }

    public static final String BASE_URL = "http://127.0.0.1:4567";
    public static final String SERVICE_PATH = "/customers";
    public static final String SERVICE_URL = BASE_URL + SERVICE_PATH;
    public static final String ACCEPT_HEADER = "accept";
    public static final String JSON_TYPE = "application/json";

    public static final class QueryParams {
        public static final String FIRST_NAME = "firstName";
        public static final String CITY = "city";
        public static final String AGE_FROM = "ageFrom";
        public static final String AGE_TO = "ageTo";

        private QueryParams() {
        }
    }
}
