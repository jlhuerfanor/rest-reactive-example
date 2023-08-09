package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.AuthorOutputDto;
import com.endava.workshops.restexample.application.model.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorToOutputDtoConverter implements Converter<Author, AuthorOutputDto> {
    @Override
    public AuthorOutputDto convert(Author source) {
        return new AuthorOutputDto(source.getId(), source.getName(), source.getCountry(), source.getEmail());
    }
}
