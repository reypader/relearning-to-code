package com.reypader.rtc.chap4.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reypader.rtc.chap4.persistence.entities.PersistedEvent;

/**
 *
 * @author rmpader
 */
public interface PersistendEventRepository extends JpaRepository<PersistedEvent, UUID> {

}
