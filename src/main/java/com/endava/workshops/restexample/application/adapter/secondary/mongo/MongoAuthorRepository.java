package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.model.Author;

@Repository
public interface MongoAuthorRepository extends AuthorRepository, ReactiveMongoRepository<Author, String> {
}
