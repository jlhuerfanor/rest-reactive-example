package com.endava.workshops.restexample.application.adapter.primary.rest;

import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.PublisherInputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.PublisherOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.PublisherManagementApi;
import com.endava.workshops.restexample.application.model.Publisher;
import com.endava.workshops.restexample.application.service.PublisherService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PublisherManagementController implements PublisherManagementApi {
    private final PublisherService publisherService;
    private final ConversionService conversionService;

    public PublisherManagementController(PublisherService publisherService, ConversionService conversionService) {
        this.publisherService = publisherService;
        this.conversionService = conversionService;
    }

    @Override
    public Mono<ResponseEntity<PublisherOutputDto>> createPublisher(Mono<PublisherInputDto> publisherInputDto, ServerWebExchange exchange) {
        return publisherInputDto
                .map(value -> conversionService.convert(value, Publisher.class))
                .flatMap(this.publisherService::add)
                .map(value -> conversionService.convert(value, PublisherOutputDto.class))
                .flatMap(value -> ResourceLinks.addPublisherResources(value, exchange))
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value));
    }

    @Override
    public Mono<ResponseEntity<Flux<BookOutputDto>>> getPublisherBooks(String id, ServerWebExchange exchange) {
        return Mono.just(id)
                .map(publisherId -> this.publisherService.getBooks(publisherId)
                        .map(value -> conversionService.convert(value, BookOutputDto.class))
                        .flatMap(value -> ResourceLinks.addBookResources(value, exchange)))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<PublisherOutputDto>> getPublisherById(String id, ServerWebExchange exchange) {
        return Mono.just(id)
                .flatMap(this.publisherService::getById)
                .map(value -> conversionService.convert(value, PublisherOutputDto.class))
                .flatMap(value -> ResourceLinks.addPublisherResources(value, exchange))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<PublisherOutputDto>>> getPublishers(ServerWebExchange exchange) {
        return Mono.just(this.publisherService.getAll()
                        .map(value -> conversionService.convert(value, PublisherOutputDto.class))
                        .flatMap(value -> ResourceLinks.addPublisherResources(value, exchange)))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.ok().body(Flux.empty())));
    }
}
