package com.snbt.customer_mgmt.bl;

import com.google.gson.Gson;
import com.snbt.customer_mgmt.domain.Customer;
import org.junit.jupiter.api.Test;

public class CustomerCreationTest {

    @Test
    void createOneCustomer() {
        Customer customer = CustomerProvider.newCustomer();
        String customerJson = new Gson().toJson(customer);
        System.out.println("======================");
        System.out.println(customerJson);
        System.out.println("======================");
    }
}
