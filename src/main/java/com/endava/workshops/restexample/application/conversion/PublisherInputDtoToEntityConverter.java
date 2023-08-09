package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.PublisherInputDto;
import com.endava.workshops.restexample.application.model.Publisher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PublisherInputDtoToEntityConverter implements Converter<PublisherInputDto, Publisher> {
    @Override
    public Publisher convert(PublisherInputDto source) {
        return Publisher.builder()
                .name(source.getName())
                .active(source.getActive())
                .website(source.getWebsite().toString())
                .build();
    }
}
