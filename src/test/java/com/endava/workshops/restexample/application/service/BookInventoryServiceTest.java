package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.BookRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookInventoryServiceTest {
    public static final String EXISTING_BOOK_TITLE = "Existing title";
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookByCriteriaQuery bookByCriteriaQuery;

    private BookInventoryService service;

    @BeforeEach
    void setup() {
        service = new BookInventoryService(bookRepository, bookByCriteriaQuery);
    }

    @Nested
    class AddBookTest {

        @Test
        void given_bookWithId_when_add_thenThrowInvalidInputException() {
            var book = Book.builder()
                    .id(Integer.toString(ThreadLocalRandom.current().nextInt()))
                    .build();

            StepVerifier.create(service.add(book))
                    .expectErrorSatisfies(error -> {
                        assertInstanceOf(InvalidInputException.class, error);
                        assertEquals(BookInventoryService.ERROR_ID_SHOULD_NOT_BE_PROVIDED, error.getMessage());
                    }).verify();
        }

        @Test
        void given_bookWithExistingTitle_when_add_thenThrowInvalidInputException() {
            var book = Book.builder()
                    .title(EXISTING_BOOK_TITLE)
                    .build();

            doReturn(Mono.just(true))
                    .when(bookRepository)
                    .existsByTitle(EXISTING_BOOK_TITLE);

            StepVerifier.create(service.add(book))
                    .expectErrorSatisfies(error -> {
                        assertInstanceOf(InvalidInputException.class, error);
                        assertEquals(BookInventoryService.ERROR_NAME_ALREADY_EXISTS, error.getMessage());
                    }).verify();
        }

        @Test
        void given_validBook_when_add_thenBookGetsPersistedInRepository() {
            var book = Book.builder()
                    .title(EXISTING_BOOK_TITLE)
                    .build();

            doReturn(Mono.just(false))
                    .when(bookRepository)
                    .existsByTitle(EXISTING_BOOK_TITLE);
            doReturn(Mono.just(book))
                    .when(bookRepository)
                    .save(book);

            StepVerifier.create(service.add(book))
                    .expectNext(book)
                    .verifyComplete();
        }
    }

    @Nested
    class UpdateBookTest {
        @Test
        void given_newBook_when_update_then_bookGetsAdded() {
            var book = Book.builder()
                    .id(Integer.toString(ThreadLocalRandom.current().nextInt()))
                    .title(EXISTING_BOOK_TITLE)
                    .build();

            doReturn(Mono.just(false))
                    .when(bookRepository)
                    .existsById(book.getId());
            doReturn(Mono.just(false))
                    .when(bookRepository)
                    .existsByTitle(EXISTING_BOOK_TITLE);
            doReturn(Mono.just(book))
                    .when(bookRepository)
                    .save(any());

            StepVerifier.create(service.update(book))
                    .expectNextMatches(value -> Objects.equals(value.getTitle(), book.getTitle()))
                    .verifyComplete();
        }

        @Test
        void given_existingBook_when_update_then_bookGetsUpdated() {
            var book = Book.builder()
                    .id(Integer.toString(ThreadLocalRandom.current().nextInt()))
                    .title(EXISTING_BOOK_TITLE)
                    .build();

            doReturn(Mono.just(true))
                    .when(bookRepository)
                    .existsById(book.getId());
            doReturn(Mono.just(book))
                    .when(bookRepository)
                    .save(book);

            StepVerifier.create(service.update(book))
                    .expectNextCount(0L)
                    .verifyComplete();
        }
    }
}