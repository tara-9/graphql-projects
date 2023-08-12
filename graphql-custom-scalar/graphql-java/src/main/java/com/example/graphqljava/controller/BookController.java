package com.example.graphqljava.controller;

import com.example.graphqljava.domain.Author;
import com.example.graphqljava.domain.Book;
import com.example.graphqljava.service.BookService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Optional;


@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @QueryMapping
    public Book bookById(@Argument String id, DataFetchingEnvironment env, GraphQLContext context) {
        return bookService.findBookById(id);
    }

    @MutationMapping
    public Book updateBookPrice(@Argument String id, @Argument int price) {
        String currency = Optional.ofNullable(httpServletRequest.getHeader("currency")).orElse("");
        if(currency.equalsIgnoreCase("euro")) return bookService.updatePriceOfBook(id, price * 2);
        return bookService.updatePriceOfBook(id, price);
    }

    @MutationMapping
    public Book updateBookPublishedDate(@Argument String id, @Argument Instant date) {
        return bookService.updatePublishedDate(id, date);
    }

    @SchemaMapping
    public Author author(Book book) {
        return bookService.findAuthorById(book.getAuthorId());
    }
}
