package com.endava.workshops.restexample.application.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.endava.workshops.restexample.application.adapter.primary.model.BookQueryParamsDto;
import com.endava.workshops.restexample.application.model.BookQueryCriteria;

@Component
public class BookQueryCriteriaDtoToEntityConverter 
        implements Converter<BookQueryParamsDto, BookQueryCriteria>{

    @Override
    @Nullable
    public BookQueryCriteria convert(BookQueryParamsDto source) {
        return BookQueryCriteria.builder()
                .title(source.getTitle())
                .author(source.getAuthor())
                .publisher(source.getPublisher())
                .yearPublished(source.getYearPublished())
                .build();
    }
    
}
