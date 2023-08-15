package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Author;
import com.endava.workshops.restexample.application.model.Book;
import org.hglteam.validation.reactive.ValidationError;
import org.hglteam.validation.reactive.Validations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorService {
    public static final String ERROR_NAME_ALREADY_EXISTS = "Name already exists";

    private final AuthorRepository authorRepository;
    private final BookByCriteriaQuery bookByCriteriaQuery;

    public AuthorService(
            AuthorRepository authorRepository,
            BookByCriteriaQuery bookByCriteriaQuery) {
        this.authorRepository = authorRepository;
        this.bookByCriteriaQuery = bookByCriteriaQuery;
    }

    public Mono<Author> add(Author author) {
        return Validations.<Author>builder()
                .onProperty(Author::getName, name -> name
                        .when(authorRepository::existsByName)
                        .then(ValidationError.withMessage(InvalidInputException::new, ERROR_NAME_ALREADY_EXISTS)))
                .validate(author)
                .flatMap(authorRepository::save);
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
