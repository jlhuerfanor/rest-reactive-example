package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.BookRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.BookQueryCriteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class BookInventoryService {
    public static final String ERROR_ID_SHOULD_NOT_BE_PROVIDED = "ID shoud not be provided!";
    
    private final BookRepository repository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public BookInventoryService(BookRepository repository, BookByCriteriaQuery bookByCriteriaQuery) {
        this.repository = repository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
    }

    public Mono<Book> add(Book book) {
        return Mono.just(book)
            .flatMap(value -> repository.existsByTitle(value.getTitle())
                    .filter(Predicate.isEqual(Boolean.FALSE))
                    .map(any -> value)
                    .switchIfEmpty(Mono.error(new InvalidInputException("Name already exists"))))
            .flatMap(repository::save);
    }

    public Mono<Book> getById(String id) {
        return Mono.just(id)
                .flatMap(repository::findById);
    }

    public Mono<Book> update(Book input) {
        return Mono.just(input.getId())
                .flatMap(repository::existsById)
                .filter(existsById -> !existsById)
                .flatMap(unused -> this.add(input))
                .switchIfEmpty(Mono.just(input)
                        .flatMap(repository::save)
                        .then(Mono.empty()));
    }

    public Mono<Book> delete(String id) {
        return repository.findById(id)
                .flatMap(book -> Mono.just(book)
                    .flatMap(repository::delete)
                    .thenReturn(book));
    }

    public Flux<Book> find(BookQueryCriteria criteria) {
        return bookByCriteriaQuery.findAllByCriteria(
            criteria.getTitle(),
            criteria.getAuthor(),
            criteria.getPublisher(),
            criteria.getYearPublished());
    }
}
