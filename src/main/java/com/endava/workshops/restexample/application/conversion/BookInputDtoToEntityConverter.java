package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.BookInputDto;
import com.endava.workshops.restexample.application.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BookInputDtoToEntityConverter 
        implements Converter<BookInputDto, Book>{

    @Override
    @Nullable
    public Book convert(BookInputDto source) {
        return Book.builder()
                .title(source.getTitle())
                .authorId(source.getAuthorId())
                .publisherId(source.getPublisherId())
                .yearPublished(source.getYearPublished())
                .build();
    }
}
