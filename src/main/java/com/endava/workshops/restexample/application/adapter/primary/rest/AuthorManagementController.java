package com.endava.workshops.restexample.application.adapter.primary.rest;

import com.endava.workshops.restexample.application.adapter.primary.model.AuthorInputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.AuthorOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.AuthorManagementApi;
import com.endava.workshops.restexample.application.model.Author;
import com.endava.workshops.restexample.application.service.AuthorService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AuthorManagementController implements AuthorManagementApi {
    private final AuthorService authorService;
    private final ConversionService conversionService;

    public AuthorManagementController(AuthorService authorService, ConversionService conversionService) {
        this.authorService = authorService;
        this.conversionService = conversionService;
    }

    @Override
    public Mono<ResponseEntity<AuthorOutputDto>> createAuthor(Mono<AuthorInputDto> authorInputDto, ServerWebExchange exchange) {
        return authorInputDto
                .map(value -> conversionService.convert(value, Author.class))
                .flatMap(this.authorService::add)
                .map(value -> conversionService.convert(value, AuthorOutputDto.class))
                .flatMap(value -> ResourceLinks.addAuthorResources(value, exchange))
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value));
    }

    @Override
    public Mono<ResponseEntity<Flux<BookOutputDto>>> getAuthorBooks(String id, ServerWebExchange exchange) {
        return Mono.just(id)
                .map(authorId -> this.authorService.getBooks(authorId)
                        .map(value -> conversionService.convert(value, BookOutputDto.class))
                        .flatMap(value -> ResourceLinks.addBookResources(value, exchange)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AuthorOutputDto>> getAuthorById(String id, ServerWebExchange exchange) {
        return Mono.just(id)
                .flatMap(this.authorService::getById)
                .map(value -> conversionService.convert(value, AuthorOutputDto.class))
                .flatMap(value -> ResourceLinks.addAuthorResources(value, exchange))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<AuthorOutputDto>>> getAuthors(ServerWebExchange exchange) {
        return Mono.just(this.authorService.getAll()
                        .map(value -> conversionService.convert(value, AuthorOutputDto.class))
                        .flatMap(value -> ResourceLinks.addAuthorResources(value, exchange)))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.ok().body(Flux.empty())));
    }
}
