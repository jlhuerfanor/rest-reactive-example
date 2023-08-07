package com.endava.workshops.restexample.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BookQueryCriteria {
    private String title;
    private String author;
    private String publisher;
    private Integer yearPublished;
}
