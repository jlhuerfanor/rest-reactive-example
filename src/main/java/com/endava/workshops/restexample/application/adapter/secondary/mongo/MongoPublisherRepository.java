package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import com.endava.workshops.restexample.application.adapter.secondary.PublisherRepository;
import com.endava.workshops.restexample.application.model.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPublisherRepository extends PublisherRepository, ReactiveMongoRepository<Publisher, String> {
    
}
