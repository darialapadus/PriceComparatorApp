package com.market.pricecomparator.model;

public class Product {
    private String name;
    private double price;
    private String store;
    private String date;
    private double grammage;
    private String unit;

    public Product() {}

    public Product(String name, double price, String store, String date, double grammage, String unit) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.date = date;
        this.grammage = grammage;
        this.unit = unit;
    }

    public double getPricePerUnit() {
        return price / grammage;
    }
}
