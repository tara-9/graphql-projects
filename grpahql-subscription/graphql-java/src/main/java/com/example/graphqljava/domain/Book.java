package com.example.graphqljava.domain;

public class Book {
    private String id;
    private String name;
    private int pageCount;
    private String authorId;
    private int price;

    public Book(String id, String name, int pageCount, String authorId, int price) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
        this.price = price;
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


}
