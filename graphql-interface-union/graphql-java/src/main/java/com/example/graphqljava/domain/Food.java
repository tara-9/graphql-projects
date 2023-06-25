package com.example.graphqljava.domain;

public class Food implements Product {

    private String id;

    private String name;

    private String link;

    private Enum<FoodType> type;

    public Food(String id, String name, String link, Enum<FoodType> type) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Enum<FoodType> getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
