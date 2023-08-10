package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Author;
import com.endava.workshops.restexample.application.model.Book;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public AuthorService(
            AuthorRepository authorRepository,
            BookByCriteriaQuery bookByCriteriaQuery) {
        this.authorRepository = authorRepository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
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

    public Flux<Book> getBooks(String authorId) {
        return bookByCriteriaQuery.findAllByAuthorId(authorId);
    }

    public Mono<Author> getById(String authorId) {
        return authorRepository.findById(authorId);
    }
}
