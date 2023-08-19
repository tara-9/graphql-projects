package com.example.graphqljava.service;

import com.example.graphqljava.domain.Author;
import com.example.graphqljava.domain.Book;
import com.example.graphqljava.util.AuthorUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    public List<Book> books;

    public BookService() {
        this.books = Arrays.asList(
                new Book("book-1", "Effective Java", 416, "author-1", 40),
                new Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2", 30),
                new Book("book-3", "Down Under", 436, "author-3", 10)
        );
    }

    public Book findBookById(String id) {
        return this.books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Author findAuthorById(String id) {
        return AuthorUtil.findAllAuthors().stream()
                .filter(author -> author.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Book updatePriceOfBook(String id, int price) {
        Book book =  this.books.stream()
                .filter(bok -> bok.getId().equals(id))
                .findFirst()
                .orElse(null);
        Optional.ofNullable(book).ifPresent(bok -> bok.setPrice(price));
        return book;
    }

    public Flux<Book> autoUpdateBookFlux(String id) {
        Flux<Book> bookFlux = Flux.generate(() -> findBookById(id) , (state, sink) -> {
           sink.next(state);
           int price = (int) (Math.random() * 100);
           state.setPrice(price);
           return state;
       });
        return bookFlux.delayElements(Duration.ofSeconds(1));
    }
}

