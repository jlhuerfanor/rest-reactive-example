package com.endava.workshops.restexample.application.conversion;

import com.endava.workshops.restexample.application.adapter.primary.model.BookOutputDto;
import com.endava.workshops.restexample.application.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BookToOutputDtoConverter 
        implements Converter<Book, BookOutputDto>{

    @Override
    @Nullable
    public BookOutputDto convert(Book source) {
        var result = new BookOutputDto(
            source.getId(),
            source.getTitle(), 
            source.getAuthorId(), 
            source.getPublisherId());

        result.setYearPublished(source.getYearPublished());

        return result;
    }
}
