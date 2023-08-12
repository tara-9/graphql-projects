package com.example.graphqljava.util;

import com.example.graphqljava.domain.Author;

import java.util.Arrays;
import java.util.List;

public class AuthorUtil {

    public static List<Author> findAllAuthors() {
        return Arrays.asList(
                new Author("author-1", "Joshua", "Bloch"),
                new Author("author-2", "Douglas", "Adams"),
                new Author("author-3", "Bill", "Bryson")
        );

    }
}
