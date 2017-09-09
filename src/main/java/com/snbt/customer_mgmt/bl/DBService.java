package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * General DB-Service interface for accessing objects managed in system's DB
 *
 * @param <E> the {@link Entity}s this DBService manages
 * @param <C> the criteria used by this DBService to filter results when fetching
 */
public interface DBService<E extends Entity, C extends Criteria<E>> {

    UUID create(E customer);

    Optional<E> getById(String id);

    List<E> getBy(C criteria);

    Optional<E> update(E toUpdate);

    void delete(String id);
}
