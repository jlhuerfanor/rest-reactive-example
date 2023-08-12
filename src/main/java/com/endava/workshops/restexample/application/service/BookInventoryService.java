package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.BookRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.BookQueryCriteria;
import org.hglteam.validation.reactive.ValidationError;
import org.hglteam.validation.reactive.Validations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class BookInventoryService {
    public static final String ERROR_ID_SHOULD_NOT_BE_PROVIDED = "ID should not be provided!";
    public static final String ERROR_NAME_ALREADY_EXISTS = "Name already exists";

    private final BookRepository repository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public BookInventoryService(BookRepository repository, BookByCriteriaQuery bookByCriteriaQuery) {
        this.repository = repository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
    }

    public Mono<Book> add(Book book) {
        return Validations.<Book>builder()
                .whenValue(value -> Objects.nonNull(value.getId()))
                .then(ValidationError.withMessage(InvalidInputException::new, ERROR_ID_SHOULD_NOT_BE_PROVIDED))
                .onProperty(Book::getTitle, title -> title
                        .when(repository::existsByTitle)
                        .then(ValidationError.withMessage(InvalidInputException::new, ERROR_NAME_ALREADY_EXISTS)))
                .validate(book)
                .flatMap(repository::save);
    }

    public Mono<Book> getById(String id) {
        return repository.findById(id);
    }

    public Mono<Book> update(Book input) {
        return repository.existsById(input.getId())
                .flatMap(result -> result
                        ? repository.save(input).then(Mono.empty())
                        : this.add(input.toBuilder().id(null).build()));
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
