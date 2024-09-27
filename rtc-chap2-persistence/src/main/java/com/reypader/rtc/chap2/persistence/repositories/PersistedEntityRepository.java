package com.reypader.rtc.chap2.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reypader.rtc.chap2.persistence.entities.PersistedEvent;

/**
 *
 * @author rmpader
 */
public interface PersistedEntityRepository extends JpaRepository<PersistedEvent, UUID> {

}
