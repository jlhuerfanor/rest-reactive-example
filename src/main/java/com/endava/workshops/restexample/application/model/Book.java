package com.endava.workshops.restexample.application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Document
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private String publisher;
    private Integer yearPublished;
}
