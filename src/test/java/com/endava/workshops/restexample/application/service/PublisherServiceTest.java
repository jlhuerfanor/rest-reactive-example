package com.endava.workshops.restexample.application.service;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.adapter.secondary.PublisherRepository;
import com.endava.workshops.restexample.application.exceptions.InvalidInputException;
import com.endava.workshops.restexample.application.model.Publisher;
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
class PublisherServiceTest {
    private static final Faker faker = new Faker();

    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private BookByCriteriaQuery bookByCriteriaQuery;

    private PublisherService publisherService;

    @BeforeEach
    void setup() {
        publisherService = new PublisherService(publisherRepository, bookByCriteriaQuery);
    }

    @Test
    void givenExistingPublisher_when_add_then_exceptionIsThrown() {
        var publisher = Publisher.builder()
                .name(faker.book().publisher())
                .build();

        doReturn(Mono.just(true))
                .when(publisherRepository)
                .existsByName(publisher.getName());

        StepVerifier.create(publisherService.add(publisher))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(InvalidInputException.class, error);
                    assertEquals(PublisherService.ERROR_NAME_ALREADY_EXISTS, error.getMessage());
                }).verify();
    }

}