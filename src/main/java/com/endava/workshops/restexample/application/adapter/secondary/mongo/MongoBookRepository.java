package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.endava.workshops.restexample.application.adapter.secondary.BookRepository;
import com.endava.workshops.restexample.application.model.Book;

@Repository
public interface MongoBookRepository extends BookRepository, ReactiveMongoRepository<Book, String> {
}
