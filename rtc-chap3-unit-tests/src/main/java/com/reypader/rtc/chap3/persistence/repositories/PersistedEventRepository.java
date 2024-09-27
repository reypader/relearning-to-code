package com.reypader.rtc.chap3.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reypader.rtc.chap3.persistence.entities.PersistedEvent;

/**
 *
 * @author rmpader
 */
public interface PersistedEventRepository extends JpaRepository<PersistedEvent, UUID> {

}
