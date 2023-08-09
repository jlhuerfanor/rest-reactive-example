package com.endava.workshops.restexample.application.adapter.secondary;

import com.endava.workshops.restexample.application.model.Author;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorRepository {
    <S extends Author> Mono<S> save(S author);
    Mono<Boolean> existsById(String id);
    Mono<Author> findById(String id);
    Mono<Boolean> existsByName(String name);
    Flux<Author> findAll();
}
