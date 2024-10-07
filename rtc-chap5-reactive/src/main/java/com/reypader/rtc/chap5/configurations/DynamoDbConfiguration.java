package com.reypader.rtc.chap5.configurations;

import java.net.URI;

import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

@Configuration
public class DynamoDbConfiguration {
    @Value("${aws.dynamodb.endpoint:http://dynamodb-local:8000}")
    private String dynamodbEndpoint;

    @Bean
    public DynamoDbAsyncClient dynamoDbClient() {
        DynamoDbAsyncClientBuilder builder = DynamoDbAsyncClient.builder();
        builder.credentialsProvider(DefaultCredentialsProvider.create());
        builder.region(Region.US_EAST_1);
        builder.endpointOverride(URI.create(dynamodbEndpoint));
        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient enhancedDynamo(DynamoDbAsyncClient dynamo) {
        return DynamoDbEnhancedAsyncClient
                .builder()
                .dynamoDbClient(dynamo)
                .build();
    }

    @Bean
    public DynamoDbAsyncTable<PersistedEvent> getCountryLocaleTable(DynamoDbEnhancedAsyncClient enhancedDynamo) {
        return enhancedDynamo.table("PERSISTED_EVENT", TableSchema.fromBean(PersistedEvent.class));
    }

}