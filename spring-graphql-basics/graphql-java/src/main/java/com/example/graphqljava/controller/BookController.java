package com.example.graphqljava.controller;

import com.example.graphqljava.domain.Author;
import com.example.graphqljava.domain.Book;
import com.example.graphqljava.service.BookService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

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
        String currency = Optional.ofNullable(httpServletRequest.getHeader("currency")).orElse("dollar");
        // If currency is euro then update price as double the amount
        if(currency.equalsIgnoreCase("euro")) return bookService.updatePriceOfBook(id, price * 2);
        return bookService.updatePriceOfBook(id, price);
    }

    @SchemaMapping
    public Author author(Book book) {
        return bookService.findAuthorById(book.getAuthorId());
    }
}
