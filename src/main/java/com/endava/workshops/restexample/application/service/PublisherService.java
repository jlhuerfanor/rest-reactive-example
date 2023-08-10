package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.PublisherRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public PublisherService(PublisherRepository publisherRepository, BookByCriteriaQuery bookByCriteriaQuery) {
        this.publisherRepository = publisherRepository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
    }

    public Mono<Publisher> add(Publisher author) {
        return Mono.just(author)
                .flatMap(value -> publisherRepository.existsByName(value.getName())
                        .filter(Predicate.isEqual(Boolean.FALSE))
                        .map(any -> value)
                        .switchIfEmpty(Mono.error(new InvalidInputException("Name already exists"))))
                .flatMap(publisherRepository::save)
                .switchIfEmpty(Mono.<Publisher>empty());
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
