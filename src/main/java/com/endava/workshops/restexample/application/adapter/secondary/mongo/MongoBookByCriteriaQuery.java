package com.endava.workshops.restexample.application.adapter.secondary.mongo;

import com.endava.workshops.restexample.application.adapter.secondary.BookByCriteriaQuery;
import com.endava.workshops.restexample.application.model.Book;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class MongoBookByCriteriaQuery implements BookByCriteriaQuery {

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoBookByCriteriaQuery(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Book> findAllByAuthorId(String authorId) {
        var query = new Query();

        query.addCriteria(Criteria.where("authorId").is(authorId));

        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public Flux<Book> findAllByPublisherId(String publisherId) {
        var query = new Query();

        query.addCriteria(Criteria.where("publisherId").is(publisherId));

        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public Flux<Book> findAllByCriteria(String title, String author, String publisher, Integer yearPublished) {
        var operations = new ArrayList<AggregationOperation>();
        
        if(Objects.nonNull(title) && !title.isBlank()) {
            operations.add(Aggregation.match(Criteria.where("title").is(title)));
        }

        if(Objects.nonNull(yearPublished)) {
            operations.add(Aggregation.match(Criteria.where("yearPublished").is(yearPublished)));
        }

        if(Objects.nonNull(author) && !author.isBlank()) {
            operations.add(Aggregation.lookup("Author", "authorId", "_id", "author"));
            operations.add(Aggregation.match(new Criteria("author.name").regex(author)));
        }

        if(Objects.nonNull(publisher) && !publisher.isBlank()) {
            operations.add(Aggregation.lookup("Publisher", "publisherId", "_id", "publisher"));
            operations.add(Aggregation.match(new Criteria("publisher.name").regex(author)));
        }

        operations.add(Aggregation.project(Book.class));

        return mongoTemplate.aggregate(
            Aggregation.newAggregation(operations), 
            Book.class, 
            Book.class);
    }
}
