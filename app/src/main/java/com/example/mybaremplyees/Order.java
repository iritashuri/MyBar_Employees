package com.example.mybaremplyees;

import android.location.LocationManager;

import java.util.ArrayList;

public class Order {

    private ArrayList<Item> item_list = new ArrayList<>();
    private String total_price;
    private String timestamp = "";
    private LocationManager location = null;
    private double lat = 0;
    private double lon = 0;


    public Order() {
        this.total_price = "0";
    }

    public Order(ArrayList<Item> item_list) {
        this.item_list = item_list;
    }

    public ArrayList<Item> addOneItemToList(Item item){
        this.item_list.add(item);
        return item_list;
    }

    public ArrayList<Item> getItem_list() {
        return item_list;
    }

    public Order setItem_list(ArrayList<Item> item_list) {
        this.item_list = item_list;
        return this;
    }

    public String getTotal_price() {
        double total = 0.0;
        for(Item item : item_list){
            total = total + Double.parseDouble(item.getPrice());
        }
        total_price = String.valueOf(total);
        return total_price;
    }
    
    public String getTimestamp() {
        return timestamp;
    }

    public Order setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public LocationManager getLocation() {
        return location;
    }

    public Order setLocation(LocationManager location) {
        this.location = location;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Order setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Order setLon(double lon) {
        this.lon = lon;
        return this;
    }
}
