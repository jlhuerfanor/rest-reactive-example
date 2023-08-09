package com.endava.workshops.restexample.application.service;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.endava.workshops.restexample.application.adapter.secondary.PublisherRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Author;
import com.endava.workshops.restexample.application.model.Publisher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
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
}
