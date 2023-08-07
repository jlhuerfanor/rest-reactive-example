package com.endava.workshops.restexample.application.adapter.primary.rest;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.endava.workshops.restexample.application.adapter.primary.model.BookInputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.BookQueryParamsDto;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.BookManagementApi;
import com.endava.workshops.restexample.application.model.Book;
import com.endava.workshops.restexample.application.model.BookQueryCriteria;
import com.endava.workshops.restexample.application.service.BookInventoryService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BookInventoryController implements BookManagementApi {
    private final BookInventoryService service;
    private final ConversionService conversionService;

    public BookInventoryController(
            BookInventoryService service,
            ConversionService conversionService) {
        this.service = service;
        this.conversionService = conversionService;
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> create(
            Mono<BookInputDto> bookInputDto,
            final ServerWebExchange exchange) {
        return bookInputDto
            .map(value -> conversionService.convert(value, Book.class))
            .flatMap(this.service::add)
            .map(result -> conversionService.convert(result, BookOutputDto.class))
            .map(result -> ResponseEntity.created(ResponseUtils.getLocationUri(exchange, result.getId()))
                    .body(result));
    }

    @Override
    public Mono<ResponseEntity<Flux<BookOutputDto>>> getByCriteria(
                BookQueryParamsDto queryParams,
                final ServerWebExchange exchange) {
        return Mono.just(Mono.just(queryParams)
                .map(value -> conversionService.convert(value, BookQueryCriteria.class))
                .flatMapMany(this.service::find)
                .map(value -> conversionService.convert(value, BookOutputDto.class)))
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> getById(
            @PathVariable("id") String id,
            final ServerWebExchange exchange){
        return Mono.just(id)
            .flatMap(this.service::getById)
            .map(value -> conversionService.convert(value, BookOutputDto.class))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<BookOutputDto>> updateOrCreate(
            String id,
            Mono<BookInputDto> bookInputDto,
            final ServerWebExchange exchange) {
        return bookInputDto
            .map(value -> conversionService.convert(value, Book.class))
            .map(value -> value.toBuilder().id(id).build())
            .flatMap(this.service::update)
            .map(value -> conversionService.convert(value, BookOutputDto.class))
            .map(value -> ResponseEntity.created(ResponseUtils.replaceLocationUri(exchange, value.getId())).body(value))
            .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("id") String id,
            final ServerWebExchange exchange) {
        return Mono.just(id)
            .map(this.service::delete)
            .map(result -> ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().<Void>build());
    }
}
