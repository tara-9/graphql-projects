package com.example.graphqljava.domain;

public class Recommendation {

    private String id;

    private String name;

    private  Integer price;

    public Recommendation(String id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }


}
