package com.endava.workshops.restexample.application.adapter.primary.rest;

import com.endava.workshops.restexample.application.adapter.primary.model.*;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.BookManagementApi;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.BookQueryCriteria;
import com.endava.workshops.restexample.application.service.AuthorService;
import com.endava.workshops.restexample.application.service.BookInventoryService;
import com.endava.workshops.restexample.application.service.PublisherService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BookManagementController implements BookManagementApi {
    private final BookInventoryService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final ConversionService conversionService;

    public BookManagementController(BookInventoryService bookService, AuthorService authorService, PublisherService publisherService, ConversionService conversionService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.conversionService = conversionService;
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> create(Mono<BookInputDto> bookInputDto, ServerWebExchange exchange) {
        return bookInputDto
                .map(value -> conversionService.convert(value, Book.class))
                .flatMap(bookService::add)
                .map(value -> conversionService.convert(value, BookOutputDto.class))
                .flatMap(value -> ResourceLinks.addBookResources(value, exchange))
                .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBook(String id, ServerWebExchange exchange) {
        return bookService.delete(id)
                .map(value -> ResponseEntity.noContent().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<AuthorOutputDto>> getBookAuthor(String id, ServerWebExchange exchange) {
        return bookService.getById(id)
                .map(Book::getAuthorId)
                .flatMap(authorService::getById)
                .map(value -> conversionService.convert(value, AuthorOutputDto.class))
                .flatMap(value -> ResourceLinks.addAuthorResources(value, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> getBookById(String id, ServerWebExchange exchange) {
        return bookService.getById(id)
                .map(value -> conversionService.convert(value, BookOutputDto.class))
                .flatMap(value -> ResourceLinks.addBookResources(value, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<PublisherOutputDto>> getBookPublisher(String id, ServerWebExchange exchange) {
        return bookService.getById(id)
                .map(Book::getPublisherId)
                .flatMap(publisherService::getById)
                .map(value -> conversionService.convert(value, PublisherOutputDto.class))
                .flatMap(value -> ResourceLinks.addPublisherResources(value, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<BookOutputDto>>> getBooksByCriteria(BookQueryParamsDto queryParams, ServerWebExchange exchange) {
        return Mono.just(queryParams)
                .map(value -> conversionService.convert(value, BookQueryCriteria.class))
                .map(value -> bookService.find(value)
                        .map(book -> conversionService.convert(book, BookOutputDto.class)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> updateOrCreateBook(String id, Mono<BookInputDto> bookInputDto, ServerWebExchange exchange) {
        return bookInputDto
                .map(value -> conversionService.convert(value, Book.class))
                .map(value -> value.toBuilder().id(id).build())
                .flatMap(bookService::update)
                .map(value -> conversionService.convert(value, BookOutputDto.class))
                .flatMap(value -> ResourceLinks.addBookResources(value, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
