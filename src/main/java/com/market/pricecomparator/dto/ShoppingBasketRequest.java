package com.market.pricecomparator.dto;

import java.util.List;

public class ShoppingBasketRequest {
    private List<String> productIds;

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
