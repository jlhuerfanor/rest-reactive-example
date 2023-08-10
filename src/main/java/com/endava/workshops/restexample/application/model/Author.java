package com.endava.workshops.restexample.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Document
public class Author {
    @Id
    private String id;
    private String name;
    private String country;
    private String email;
}
