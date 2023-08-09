package com.endava.workshops.restexample.application.adapter.secondary;

import com.endava.workshops.restexample.application.model.Book;

import reactor.core.publisher.Mono;

public interface BookRepository {    
  <S extends Book> Mono<S> save(S book);
  Mono<Void> delete(Book book);
  Mono<Boolean> existsById(String id);
  Mono<Boolean> existsByTitle(String name);
  Mono<Book> findById(String id);
}
