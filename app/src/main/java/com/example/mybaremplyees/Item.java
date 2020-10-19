package com.example.mybaremplyees;



public class Item {

    public interface CATEGORIES {
        public static final String SOFT_DRINKS = "SOFT_DRINKS";
        public static final String COCKTAILS = "COCKTAILS";
        public static final String BEERS = "BEERS";
        public static final String WINES = "WINES";
        public static final String CHASERS = "CHASERS";
        public static final String DEALS = "DEALS";
        public static final String FOOD = "FOOD";

    }

    private String category;
    private String description;
    private String price;
    private String key;

    public Item() {
    }


    public Item(String description, String price) {
        this.category = category;
        this.price = price;
    }


    public Item(String category, String description, String price) {
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public Item setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Item setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Item setKey(String key) {
        this.key = key;
        return this;
    }
}
