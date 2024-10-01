package com.reypader.rtc.chap5.persistence.repositories;

import java.util.Collection;
import java.util.UUID;
import java.util.Optional;

import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;

/**
 *
 * @author rmpader
 */
public interface PersistedEventRepository {

    PersistedEvent save(PersistedEvent fromResource);

    Optional<PersistedEvent> findById(UUID id);

    Collection<PersistedEvent> findAll();

}
