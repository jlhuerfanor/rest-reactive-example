package com.endava.workshops.restexample.application.adapter.primary.rest;

import com.endava.workshops.restexample.application.adapter.primary.model.AuthorOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.model.PublisherOutputDto;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.AuthorManagementApi;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.BookManagementApi;
import com.endava.workshops.restexample.application.adapter.primary.rest.api.PublisherManagementApi;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public final class ResourceLinks {

    public static Mono<AuthorOutputDto> addAuthorResources(AuthorOutputDto author, ServerWebExchange exchange) {
        var selfLink = linkTo(methodOn(AuthorManagementApi.class)
                        .getAuthorById(author.getId(), exchange),
                exchange).withSelfRel();
        var booksLink = linkTo(methodOn(AuthorManagementApi.class)
                        .getAuthorBooks(author.getId(), exchange),
                exchange).withRel("books");

        return Flux.merge(selfLink.toMono(), booksLink.toMono())
                .map(author::add)
                .last();
    }

    private ResourceLinks() { }

    public static Mono<BookOutputDto> addBookResources(BookOutputDto book, ServerWebExchange exchange) {
        var selfLink = linkTo(methodOn(BookManagementApi.class)
                        .getBookById(book.getId(), exchange),
                exchange).withSelfRel();
        var authorLink = linkTo(methodOn(BookManagementApi.class)
                        .getBookAuthor(book.getId(), exchange),
                exchange).withRel("author");
        var publisherLink = linkTo(methodOn(BookManagementApi.class)
                        .getBookPublisher(book.getId(), exchange),
                exchange).withRel("publisher");

        return Flux.merge(selfLink.toMono(), authorLink.toMono(), publisherLink.toMono())
                .map(book::add)
                .last();
    }

    public static Mono<PublisherOutputDto> addPublisherResources(PublisherOutputDto value, ServerWebExchange exchange) {
        var selfLink = linkTo(methodOn(PublisherManagementApi.class)
                        .getPublisherById(value.getId(), exchange),
                exchange).withSelfRel();
        var booksLink = linkTo(methodOn(PublisherManagementApi.class)
                        .getPublisherBooks(value.getId(), exchange),
                exchange).withRel("books");

        return Flux.merge(selfLink.toMono(), booksLink.toMono())
                .map(value::add)
                .last();
    }
}
