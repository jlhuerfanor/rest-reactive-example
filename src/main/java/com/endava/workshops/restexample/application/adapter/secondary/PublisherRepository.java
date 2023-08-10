package com.endava.workshops.restexample.application.adapter.secondary;

import com.endava.workshops.restexample.application.model.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PublisherRepository {
    <S extends Publisher> Mono<S> save(S author);
    Mono<Boolean> existsById(String id);
    Mono<Publisher> findById(String id);
    Mono<Boolean> existsByName(String name);
    Flux<Publisher> findAll();
}
