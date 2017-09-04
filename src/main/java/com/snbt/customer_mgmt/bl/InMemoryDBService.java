package com.snbt.customer_mgmt.bl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.snbt.customer_mgmt.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * In-memory customer DB implementation that maintains all entries in java map.
 * The map is initialized from db.json file during startup, and flushed during shutdown (poor man's persistence)
 */
public class InMemoryDBService implements DBService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryDBService.class);

    private Map<UUID, Customer> inMemoryDB;


    public InMemoryDBService() {
        inMemoryDB = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            inMemoryDB.putAll(loadData());
        } catch (Exception e) {
            log.error("An error occurred while loading customer DB. An empty in-memory db initialized", e);
        }
    }

    private Map<UUID, Customer> loadData() throws FileNotFoundException {
        log.info("Loading DB from file");
        JsonReader reader = new JsonReader(new FileReader("db.json"));
        Type customerType = new TypeToken<Map<UUID, Customer>>() {
        }.getType();
        Map<UUID, Customer> customerDb = new Gson().fromJson(reader, customerType);
        log.info("Loaded {} entries from customer db file", customerDb.size());

        return customerDb;
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

    public void stop() {
        try {
            flushData();
        } catch (IOException e) {
            log.error("An exception occurred while stop db service", e);
        }
    }

    private void flushData() throws IOException {
        log.debug("Flushing {} customer entries into db.json", inMemoryDB.size());
        try (Writer writer = new FileWriter("db.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(inMemoryDB, writer);
        }
    }
}
