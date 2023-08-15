package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.AuthorRepository;
import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Author;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    private static final Faker faker = new Faker();

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookByCriteriaQuery bookByCriteriaQuery;

    private AuthorService authorService;

    @BeforeEach
    void setup() {
        this.authorService = new AuthorService(authorRepository, bookByCriteriaQuery);
    }

    @Test
    void given_existingAuthor_when_add_thenExceptionIsThrown() {
        var author = Author.builder()
                .name(faker.book().author())
                .build();

        doReturn(Mono.just(true))
                .when(authorRepository)
                .existsByName(author.getName());

        StepVerifier.create(authorService.add(author))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(InvalidInputException.class, error);
                    assertEquals(AuthorService.ERROR_NAME_ALREADY_EXISTS, error.getMessage());
                }).verify();
    }
}