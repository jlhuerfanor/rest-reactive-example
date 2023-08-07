package com.endava.workshops.restexample.infrastructure;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories("com.endava.workshops.restexample.application.adapter.secondary.mongo")
public class MongoDBConfiguration extends AbstractReactiveMongoConfiguration {

    private static final String MONGOBD_NAME = "library";

    private final String host;
    private final Integer port;
    private final String username;
    private final String password;

    public MongoDBConfiguration(
            @Value("${spring.mongodb.host}") String host
            , @Value("${spring.mongodb.port}") Integer port
            , @Value("${spring.mongodb.username}") String username
            , @Value("${spring.mongodb.password}") String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String getDatabaseName() {
        return MONGOBD_NAME;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s@%s:%d/"
                        , URLEncoder.encode(username, StandardCharsets.UTF_8)
                        , URLEncoder.encode(password, StandardCharsets.UTF_8)
                        , host
                        , port)))
                .build());
    }
}
