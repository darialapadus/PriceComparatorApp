package com.market.pricecomparator.dto;

public class BasketItemDTO {
    private String productId;
    private String productName;
    private String store;
    private double price;
    private String unit;
    private double quantity;

    public BasketItemDTO(String productId, String productName, String store, double price, String unit, double quantity) {
        this.productId = productId;
        this.productName = productName;
        this.store = store;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getStore() {
        return store;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public double getQuantity() {
        return quantity;
    }
}
