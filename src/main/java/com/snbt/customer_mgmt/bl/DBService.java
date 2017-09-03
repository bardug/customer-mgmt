package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface DBService {

    UUID create(Customer customer);

    Optional<Customer> getById(String id);

    List<Customer> getBy(Predicate<? super Customer> criteria);

    Optional<Customer> update(Customer toUpdate);

    void delete(String id);
}
