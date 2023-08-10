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
public class Publisher {
    @Id
    private String id;
    private String name;
    private boolean active;
    private String website;
}
