package com.endava.workshops.restexample.application.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.model.Book;

@Component
public class BookToOutputDtoConverter 
        implements Converter<Book, BookOutputDto>{

    @Override
    @Nullable
    public BookOutputDto convert(Book source) {
        return BookOutputDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .author(source.getAuthor())
                .publisher(source.getPublisher())
                .yearPublished(source.getYearPublished())
                .build();
    }
}
