package com.reypader.rtc.chap4.persistence.repositories;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.ServerSocket;


/*
 * Taken from
 * https://zdenek-papez.medium.com/spring-boot-integration-tests-using-aws-dynamodb-local-with-maven-7dba6ca2ccb9
 */
public class LocalDbCreationRule implements AfterAllCallback, BeforeAllCallback {
    private DynamoDBProxyServer server;

    private String port;


    public String getPort() {
        return port;
    }

    public LocalDbCreationRule() {
        System.setProperty("aws.accessKeyId", "test");
        System.setProperty("aws.secretAccessKey", "test");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = String.valueOf(serverSocket.getLocalPort());
        }
        server = ServerRunner.createServerFromCommandLineArgs(
                new String[]{"-inMemory", "-port", port});
        server.start();
    }


    @Override
    public void afterAll(ExtensionContext context) {
        this.stopUnchecked(server);
    }

    protected void stopUnchecked(DynamoDBProxyServer dynamoDbServer) {
        try {
            dynamoDbServer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}