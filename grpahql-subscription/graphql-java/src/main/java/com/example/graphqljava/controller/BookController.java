package com.example.graphqljava.controller;

import com.example.graphqljava.domain.Author;
import com.example.graphqljava.domain.Book;
import com.example.graphqljava.service.BookService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;


@Controller
public class BookController {

    @Autowired
    BookService bookService;


    @QueryMapping
    public Book bookById(@Argument String id, DataFetchingEnvironment env, GraphQLContext context) {
        return bookService.findBookById(id);
    }

    @SubscriptionMapping
    public Flux<Book> bookPrice(@Argument String id) {
        return bookService.autoUpdateBookFlux(id);
    }

    @SchemaMapping
    public Author author(Book book) {
        return bookService.findAuthorById(book.getAuthorId());
    }
}
