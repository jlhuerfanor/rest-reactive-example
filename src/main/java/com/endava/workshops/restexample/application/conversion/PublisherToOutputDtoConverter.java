package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.PublisherOutputDto;
import com.endava.workshops.restexample.application.model.Publisher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class PublisherToOutputDtoConverter implements Converter<Publisher, PublisherOutputDto> {
    @Override
    public PublisherOutputDto convert(Publisher source) {
        return new PublisherOutputDto(source.getId(), source.getName(), source.isActive(),
                URI.create(source.getWebsite()));
    }
}
