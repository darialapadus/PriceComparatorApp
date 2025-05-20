package com.market.pricecomparator.model;

public class Discount {
    private String productId;
    private String productName;
    private String brand;
    private double quantity;
    private String unit;
    private String category;
    private String fromDate;
    private String toDate;
    private double percentage;
    private String store;

    public Discount() {}

    public double getPercentage() {
        return percentage;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }
}
