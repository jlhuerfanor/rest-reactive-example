package com.endava.workshops.restexample.application.adapter.secondary;

import com.endava.workshops.restexample.application.model.Book;

import reactor.core.publisher.Flux;

public interface BookByCriteriaQuery {
    Flux<Book> findAllByCriteria(
        String title, 
        String author, 
        String publisher,
        Integer yearPublished);
}
