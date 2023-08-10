package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import com.endava.workshops.restexample.application.adapter.secondary.BookRepository;
import com.endava.workshops.restexample.application.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBookRepository extends BookRepository, ReactiveMongoRepository<Book, String> {
}
