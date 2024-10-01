package com.reypader.rtc.chap5.persistence.repositories;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

/**
 * @author rmpader
 */
@Component
public class PersistedEventDynamoRepository implements PersistedEventRepository {

    private final DynamoDbTable<PersistedEvent> table;

    @Autowired
    public PersistedEventDynamoRepository(DynamoDbTable<PersistedEvent> table) {
        this.table = table;
    }

    @Override
    public PersistedEvent save(PersistedEvent fromResource) {
        table.putItem(fromResource);
        return fromResource;
    }

    @Override
    public Optional<PersistedEvent> findById(UUID id) {
        return Optional.ofNullable(table.getItem(Key.builder().partitionValue(id.toString()).build()));
    }

    @Override
    public Collection<PersistedEvent> findAll() {
        return table.scan().items().stream().toList();
    }

}
