package com.reypader.rtc.chap4.configurations;

import java.net.URI;

import com.reypader.rtc.chap4.persistence.entities.PersistedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.extensions.VersionedRecordExtension;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
public class DynamoDbConfiguration {
    @Value("${aws.dynamodb.endpoint:http://dynamodb-local:8000}")
    private String dynamodbEndpoint;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();
        builder.credentialsProvider(DefaultCredentialsProvider.create());
        builder.region(Region.US_EAST_1);
        builder.endpointOverride(URI.create(dynamodbEndpoint));
        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedClient enhancedDynamo(DynamoDbClient dynamo) {
        return DynamoDbEnhancedClient
                .builder()
                .dynamoDbClient(dynamo)
                .build();
    }

    @Bean
    public DynamoDbTable<PersistedEvent> getCountryLocaleTable(DynamoDbEnhancedClient enhancedDynamo) {
        return enhancedDynamo.table("PERSISTED_EVENT", TableSchema.fromBean(PersistedEvent.class));
    }

}