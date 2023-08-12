package com.example.graphqljava.domain;

import java.time.Instant;

public class Book {
    private String id;
    private String name;
    private int pageCount;
    private String authorId;
    private int price;
    private Instant publishedDate;

    public Book(String id, String name, int pageCount, String authorId, int price, Instant publishedDate) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
        this.price = price;
        this.publishedDate = publishedDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
       return name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }


}
