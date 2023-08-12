package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.PublisherRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.Publisher;
import org.hglteam.validation.reactive.ValidationError;
import org.hglteam.validation.reactive.Validations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class PublisherService {
    public static final String ERROR_NAME_ALREADY_EXISTS = "Name already exists";

    private final PublisherRepository publisherRepository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public PublisherService(PublisherRepository publisherRepository, BookByCriteriaQuery bookByCriteriaQuery) {
        this.publisherRepository = publisherRepository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
    }

    public Mono<Publisher> add(Publisher author) {
        return Validations.<Publisher>builder()
                .onProperty(Publisher::getName, name -> name
                        .when(publisherRepository::existsByName)
                        .then(ValidationError.withMessage(InvalidInputException::new, ERROR_NAME_ALREADY_EXISTS)))
                .validate(author)
                .flatMap(publisherRepository::save);
    }

    public Flux<Publisher> getAll() {
        return publisherRepository.findAll();
    }

    public Flux<Book> getBooks(String publisherId) {
        return bookByCriteriaQuery.findAllByPublisherId(publisherId);
    }

    public Mono<Publisher> getById(String publisherId) {
        return publisherRepository.findById(publisherId);
    }
}
