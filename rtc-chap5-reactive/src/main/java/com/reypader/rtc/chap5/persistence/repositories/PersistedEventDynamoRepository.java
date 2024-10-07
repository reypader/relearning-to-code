package com.reypader.rtc.chap5.persistence.repositories;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

/**
 * @author rmpader
 */
@Component
public class PersistedEventDynamoRepository implements PersistedEventRepository {

    private final DynamoDbAsyncTable<PersistedEvent> table;

    @Autowired
    public PersistedEventDynamoRepository(DynamoDbAsyncTable<PersistedEvent> table) {
        this.table = table;
    }

    @Override
    public Mono<PersistedEvent> save(PersistedEvent fromResource) {
        return Mono.fromFuture(table.putItem(fromResource)).thenReturn(fromResource);
    }

    @Override
    public Mono<PersistedEvent> findById(UUID id) {
        return Mono.fromFuture(table.getItem(Key.builder().partitionValue(id.toString()).build()))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Flux<PersistedEvent> findAll() {
        return Flux.from(table.scan().items());
    }

}
