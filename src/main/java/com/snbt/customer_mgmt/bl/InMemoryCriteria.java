package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Customer;

import java.time.LocalDate;
import java.util.function.Predicate;

public class InMemoryCriteria implements Criteria<Customer> {

    private Predicate<Customer> predicate;

    private InMemoryCriteria(Predicate<Customer> predicate) {
        this.predicate = predicate;
    }

    public static InMemoryCriteria from(Predicate<Customer> predicate) {
        return new InMemoryCriteria(predicate);
    }

    Predicate<Customer> predicate() {
        return predicate;
    }

    public static class Predicates {
        private Predicates() {
        }
        public static Predicate<Customer> everything() {
            return c -> true;
        }

        public static Predicate<Customer> firstNameIs(String firstName) {
            return c -> c.getFirstName().equals(firstName);
        }

        public static Predicate<Customer> cityIs(String city) {
            return c -> c.getAddress().getCity().equals(city);
        }

        public static Predicate<Customer> ageFrom(String ageFrom) {
            return c -> c.getDateOfBirth().plusYears(Long.parseLong(ageFrom)).isBefore(LocalDate.now());
        }

        public static Predicate<Customer> ageTo(String ageTo) {
            return c -> c.getDateOfBirth().plusYears(Long.parseLong(ageTo)).isAfter(LocalDate.now());
        }

        public static Predicate<Customer> creditCardExpired() {
            return c -> c.getCreditCard().getExpiry().isBefore(LocalDate.now());
        }
    }
}
