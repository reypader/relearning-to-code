package com.reypader.rtc.chap5.persistence.repositories;

import com.reypader.rtc.chap5.configurations.DynamoDbConfiguration;
import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {DynamoDbConfiguration.class, PersistedEventDynamoRepository.class})
public class PersistedEventDynamoRepositoryTest {
    @RegisterExtension
    static LocalDbCreationRule localDynamoDb = new LocalDbCreationRule();

    @DynamicPropertySource
    static void localDynamoDbProperties(DynamicPropertyRegistry registry) {
        String port = localDynamoDb.getPort();
        registry.add("aws.dynamodb.endpoint", () ->
                String.format("http://localhost:%s", port));
    }

    @Autowired
    private PersistedEventDynamoRepository repo;
    @Autowired
    private DynamoDbAsyncTable<PersistedEvent> countryTable;

    @BeforeEach
    public void beforeEach() {
        StepVerifier.create(Mono.fromFuture(countryTable.createTable())).verifyComplete();
    }

    @AfterEach
    public void afterEach() {
        StepVerifier.create(Mono.fromFuture(countryTable.deleteTable())).verifyComplete();
    }

    @Test
    public void itShouldGetListOfCountries() throws Exception {
        PersistedEvent event = new PersistedEvent();
        event.setEventName("A");

        StepVerifier.create(repo.save(event))
                .assertNext(saved -> assertEquals(event, saved))
                .verifyComplete();

        StepVerifier.create(repo.findById(event.getId()))
                .assertNext(found -> assertEquals(event, found))
                .verifyComplete();

        StepVerifier.create(repo.findAll())
                .assertNext(found -> assertEquals(event, found))
                .verifyComplete();

    }
}
