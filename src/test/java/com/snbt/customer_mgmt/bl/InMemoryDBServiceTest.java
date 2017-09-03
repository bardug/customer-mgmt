package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.snbt.customer_mgmt.bl.BuiltInCriteria.creditCardExpired;
import static com.snbt.customer_mgmt.bl.BuiltInCriteria.everything;
import static com.snbt.customer_mgmt.bl.BuiltInCriteria.firstNameIs;
import static com.snbt.customer_mgmt.bl.CustomerProvider.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

class InMemoryDBServiceTest {

    private static final String CUSTOMER_FIRST_ART = "Art";

    private InMemoryDBService testedService;

    @BeforeEach
    void setUp() {
        testedService = new InMemoryDBService();
    }

    @Test
    @DisplayName("Create some customers and retrieve them all")
    void createSeveralAndRetrieveAll() {
        Customer givenCustomer1 = newCustomer();
        Customer givenCustomer2 = newCustomer();
        Customer givenCustomer3 = newCustomer();
        Customer[] givenCustomers = {givenCustomer1, givenCustomer2, givenCustomer3};

        testedService.create(givenCustomer1);
        testedService.create(givenCustomer2);
        testedService.create(givenCustomer3);

        List<Customer> allCustomers = testedService.getBy(everything());

        assertThat("Unexpected list of customers returned", allCustomers, containsInAnyOrder(givenCustomers));
    }

    @Test
    @DisplayName("New single customer is created and retrieved by a simple predicate")
    void newCustomerCreateAndRetrieve() {
        Customer givenCustomer = newCustomerNamed(CUSTOMER_FIRST_ART);
        testedService.create(givenCustomer);
        List<Customer> customersNamedArt = testedService.getBy(firstNameIs(CUSTOMER_FIRST_ART));
        assertThat("Expected a single element list", 1, is(customersNamedArt.size()));
        assertThat("Returned customer is not as expected", givenCustomer, is(customersNamedArt.get(0)));
    }

    @Test
    @DisplayName("Several customers created and retrieved by a bit more complicated criteria")
    void someCustomersCreateAndRetrieve() {
        Customer artVandelay = newCustomer();
        Customer lomez = newCustomerNamed("Lomez");
        Customer bobSacamano = newCustomerWithCredit(newCreditCardExpiresIn(LocalDate.now().minusDays(3)));
        bobSacamano.setFirstName("Bob");
        bobSacamano.setLastName("Sacamano");

        testedService.create(artVandelay);
        testedService.create(bobSacamano);
        testedService.create(lomez);
        testedService.create(artVandelay);

        List<Customer> namedBobAndCardIsExpired = testedService.getBy(firstNameIs("Bob").and(creditCardExpired()));

        assertThat("There should be only one customer named Bob with an expired credit card", 1, is(namedBobAndCardIsExpired.size()));
        assertThat("Returned customer is not Bob Sacamano", bobSacamano, is(namedBobAndCardIsExpired.get(0)));
    }


}