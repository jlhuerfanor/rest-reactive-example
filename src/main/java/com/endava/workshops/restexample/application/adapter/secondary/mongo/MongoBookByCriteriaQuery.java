package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import java.util.Objects;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.model.Book;

import reactor.core.publisher.Flux;

@Component
public class MongoBookByCriteriaQuery implements BookByCriteriaQuery {

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoBookByCriteriaQuery(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Book> findAllByCriteria(String title, String author, String publisher, Integer yearPublished) {
        var query = new Query();
        
        if(Objects.nonNull(title) && !title.isBlank()) {
            query.addCriteria(Criteria.where("title").regex(title));
        }

        if(Objects.nonNull(author) && !author.isBlank()) {
            query.addCriteria(Criteria.where("author").regex(title));
        }

        if(Objects.nonNull(publisher) && !publisher.isBlank()) {
            query.addCriteria(Criteria.where("publisher").regex(title));
        }

        if(Objects.nonNull(yearPublished)) {
            query.addCriteria(Criteria.where("title").is(yearPublished));
        }

        return mongoTemplate.find(query, Book.class);
    }
    
}
