package com.example.mybaremplyees;

import java.util.UUID;

public class Deal {
    String key;
    private String description;
    private String price;

    public Deal() {
        this.key = UUID.randomUUID().toString();
    }

    public Deal(String description, String price) {
        this.key = UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
    }


    public String getKey() {
        return key;
    }

    public Deal setKey(String key) {
        this.key = key;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Deal setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Deal setPrice(String price) {
        this.price = price;
        return this;
    }
}
