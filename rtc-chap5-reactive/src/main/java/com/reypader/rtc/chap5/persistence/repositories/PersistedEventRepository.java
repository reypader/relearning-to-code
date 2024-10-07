package com.reypader.rtc.chap5.persistence.repositories;

import java.util.Collection;
import java.util.UUID;
import java.util.Optional;

import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author rmpader
 */
public interface PersistedEventRepository {

    Mono<PersistedEvent> save(PersistedEvent fromResource);

    Mono<PersistedEvent> findById(UUID id);

    Flux<PersistedEvent> findAll();

}
