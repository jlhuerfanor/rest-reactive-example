package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.model.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoAuthorRepository extends AuthorRepository, ReactiveMongoRepository<Author, String> {
}
