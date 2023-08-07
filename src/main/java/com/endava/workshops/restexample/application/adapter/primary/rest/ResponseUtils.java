package com.endava.workshops.restexample.application.adapter.primary.rest;

import java.net.URI;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

public final class ResponseUtils {
    public static URI getLocationUri(ServerWebExchange exchange, Object id) {
        return UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();
    }

    public static URI replaceLocationUri(ServerWebExchange exchange, Object id) {
        return UriComponentsBuilder
                    .fromUri(exchange.getRequest().getURI())
                    .path("/../{id}")
                    .buildAndExpand(id)
                    .normalize()
                    .toUri();
    }

    private ResponseUtils() { }
}
