package com.example.graphqljava.controller;

import com.example.graphqljava.domain.Author;
import com.example.graphqljava.domain.Book;
import com.example.graphqljava.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @QueryMapping
    public Book bookById(@Argument String id) {
        return bookService.findBookById(id);
    }

    @MutationMapping
    public Book updateBookPrice(@Argument String id, @Argument int price) {
        return bookService.updatePriceOfBook(id, price);
    }

    @SchemaMapping
    public Author author(Book book) {
        return bookService.findAuthorById(book.getAuthorId());
    }
}
