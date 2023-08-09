package com.endava.workshops.restexample.application.service;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Author;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Mono<Author> add(Author author) {
        return Mono.just(author)
                .flatMap(value -> authorRepository.existsByName(value.getName())
                        .filter(Predicate.isEqual(Boolean.FALSE))
                        .map(any -> value)
                        .switchIfEmpty(Mono.error(new InvalidInputException("Name already exists"))))
                .flatMap(authorRepository::save)
                .switchIfEmpty(Mono.<Author>empty());
    }

    public Flux<Author> getAll() {
        return authorRepository.findAll();
    }
}
