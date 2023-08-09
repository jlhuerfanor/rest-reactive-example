package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.AuthorInputDto;
import com.endava.workshops.restexample.application.model.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorInputDtoToEntityConverter
        implements Converter<AuthorInputDto, Author> {
    @Override
    public Author convert(AuthorInputDto source) {
        return Author.builder()
                .name(source.getName())
                .country(source.getCountry())
                .email(source.getEmail())
                .build();
    }
}
