package com.example.graphqljava.domain;

public class Clothing implements Product {

    private String id;

    private String name;

    private String link;

    private String size;

    public Clothing(String id, String name, String link, String size) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.size = size;
    }


    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getSize() {
        return size;
    }

    public String getId() {
        return id;
    }
}
