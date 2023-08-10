package com.endava.workshops.restexample.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Document
public class Book {
    @Id
    private String id;
    private String title;
    private Integer yearPublished;

    @Field(targetType = FieldType.OBJECT_ID)
    private String authorId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String publisherId;
}
