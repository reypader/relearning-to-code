package com.reypader.rtc.chap4.persistence.repositories;

import com.reypader.rtc.chap4.configurations.DynamoDbConfiguration;
import com.reypader.rtc.chap4.persistence.entities.PersistedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
    private DynamoDbTable<PersistedEvent> countryTable;

    @BeforeEach
    public void beforeEach() {
        countryTable.createTable();
    }

    @AfterEach
    public void afterEach() {
        countryTable.deleteTable();
    }

    @Test
    public void itShouldGetListOfCountries() throws Exception {
        PersistedEvent event = new PersistedEvent();
        event.setEventName("A");
        PersistedEvent saved = repo.save(event);
        assertEquals(event, saved);
        Optional<PersistedEvent> found = repo.findById(event.getId());
        assertEquals(event, found.get());
        Collection<PersistedEvent> foundAll = repo.findAll();
        assertEquals(1, foundAll.size());
        assertEquals(event, foundAll.stream().findFirst().get());
    }
}
