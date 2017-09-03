package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryDBService implements DBService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryDBService.class);

    private Map<UUID, Customer> inMemoryDB;


    public InMemoryDBService() {
        inMemoryDB = new ConcurrentHashMap<>();
    }

    @Override
    public UUID create(Customer customer) {
        log.debug("Creating new customer: {}", customer);
        inMemoryDB.put(customer.getId(), customer);
        return customer.getId();
    }

    @Override
    public Optional<Customer> getById(String id) {
        log.debug("Fetching customer with ID {}", id);
        Customer fetched = inMemoryDB.get(UUID.fromString(id));
        log.debug("Customer fetched: {}", fetched);
        return Optional.ofNullable(fetched);
    }

    @Override
    public List<Customer> getBy(Predicate<? super Customer> predicate) {
        log.debug("Fetching customers by criteria");
        List<Customer> fetched = inMemoryDB.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        log.debug("{} customers fetched", fetched.size());
        return fetched;
    }

    @Override
    public Optional<Customer> update(Customer toUpdate) {
        UUID id = toUpdate.getId();
        log.debug("Updating customer with ID {}", id);
        Customer existing = inMemoryDB.get(id);
        if (existing != null) {
            Customer updated = existing.updateBy(toUpdate);
            inMemoryDB.put(id, updated);
            log.debug("Customer with ID {} updated", id);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String id) {
        log.debug("Deleting customer with ID {}", id);
        inMemoryDB.remove(UUID.fromString(id));
        log.debug("Customer with ID {} deleted", id);
    }
}
