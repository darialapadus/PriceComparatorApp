package com.market.pricecomparator.dto;

public class PricePointDTO {
    private String date;
    private double price;

    public PricePointDTO(String date, double price) {
        this.date = date;
        this.price = price;
    }

    public String getDate() { return date; }
    public double getPrice() { return price; }
}
