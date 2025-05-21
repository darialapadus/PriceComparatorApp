package com.market.pricecomparator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.pricecomparator.controller.DiscountController;
import com.market.pricecomparator.controller.ProductController;
import com.market.pricecomparator.dto.PriceAlertRequest;
import com.market.pricecomparator.dto.ShoppingBasketRequest;
import com.market.pricecomparator.model.Discount;
import com.market.pricecomparator.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(controllers = { ProductController.class, DiscountController.class })
public class PriceComparatorAdvancedTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvProductService csvProductService;

    @MockBean
    private CsvDiscountService discountService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCheckPriceAlert_returnsMatchingProducts() throws Exception {
        Product product = new Product();
        product.setProductId("P001");
        product.setProductName("lapte zuzu");
        product.setStore("Lidl");
        product.setPrice(9.5);
        product.setPackageUnit("l");
        product.setPackageQuantity(1);

        when(csvProductService.loadAllHistoricalProducts()).thenReturn(List.of(product));

        PriceAlertRequest request = new PriceAlertRequest();
        request.setProductId("P001");
        request.setTargetPrice(10.0);
        request.setStore("Lidl");

        mockMvc.perform(post("/api/products/alert")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value("P001"));
    }

    @Test
    void testCompareValuePerUnit_returnsLowestPricePerUnit() throws Exception {
        Product p1 = new Product("P001", "lapte A", "lactate", "Zuzu", 1, "l", 9.5, "RON", "Lidl", "file.csv");
        Product p2 = new Product("P001", "lapte A", "lactate", "Zuzu", 1, "l", 10.0, "RON", "Profi", "file.csv");

        when(csvProductService.loadStandardProductsFromCsv(anyString(), anyString())).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/products/compare/value-per-unit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value("P001"))
                .andExpect(jsonPath("$[0].store").value("Lidl"));
    }

    @Test
    void testGetTopDiscounts_returnsSortedDiscounts() throws Exception {
        Discount d1 = new Discount(); d1.setProductId("P001"); d1.setPercentage(20); d1.setStore("Lidl");
        Discount d2 = new Discount(); d2.setProductId("P002"); d2.setPercentage(30); d2.setStore("Profi");

        when(discountService.getTopDiscounts(2)).thenReturn(List.of(d2, d1));

        mockMvc.perform(get("/api/discounts/top?limit=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value("P002"));
    }

    @Test
    void testGetNewDiscounts_returnsOnlyNewOnes() throws Exception {
        Discount d = new Discount();
        d.setProductId("P003");
        d.setStore("Kaufland");
        d.setPercentage(10.0);

        when(discountService.getNewDiscounts()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/discounts/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value("P003"));
    }

    @Test
    void testGetPriceHistory_returnsGroupedByStore() throws Exception {
        Product p1 = new Product("P001", "lapte", "lactate", "Zuzu", 1, "l", 10.0, "RON", "Lidl", "2025-05-01");
        Product p2 = new Product("P001", "lapte", "lactate", "Zuzu", 1, "l", 9.5, "RON", "Lidl", "2025-05-08");

        when(csvProductService.loadAllHistoricalProducts()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/products/history/P001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Lidl.length()").value(2))
                .andExpect(jsonPath("$.Lidl[0].price").value(10.0))
                .andExpect(jsonPath("$.Lidl[1].price").value(9.5));
    }

    @Test
    void testOptimizeBasket_returnsCheapestProducts() throws Exception {
        Product product1 = new Product();
        product1.setProductId("P001");
        product1.setProductName("lapte zuzu");
        product1.setStore("Lidl");
        product1.setPrice(9.8);
        product1.setPackageUnit("l");
        product1.setPackageQuantity(1);

        Product product2 = new Product();
        product2.setProductId("P002");
        product2.setProductName("iaurt grecesc");
        product2.setStore("Profi");
        product2.setPrice(10.5);
        product2.setPackageUnit("kg");
        product2.setPackageQuantity(0.4);

        when(csvProductService.loadAllHistoricalProducts()).thenReturn(List.of(product1, product2));

        ShoppingBasketRequest request = new ShoppingBasketRequest();
        request.setProductIds(List.of("P001", "P002"));

        mockMvc.perform(post("/api/products/basket/optimize")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value("P001"))
                .andExpect(jsonPath("$[0].store").value("Lidl"))
                .andExpect(jsonPath("$[1].productId").value("P002"))
                .andExpect(jsonPath("$[1].store").value("Profi"));
    }
}
