package com.market.pricecomparator.service;

import com.market.pricecomparator.model.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvProductServiceTest {

    private final CsvProductService service = new CsvProductService();

    @Test
    void testLoadProductsFromCsv_validFile_shouldLoadProducts() {
        String fileName = "lidl_2025-05-08.csv";

        List<Product> products = service.loadStandardProductsFromCsv(fileName, "Lidl");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("lapte zuzu", products.get(0).getProductName());
    }

    @Test
    void testLoadProductsFromCsv_invalidFile_shouldReturnEmptyList() {
        String fileName = "file_not_exist.csv";

        List<Product> products = service.loadStandardProductsFromCsv(fileName, "Unknown");

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void testLoadAllHistoricalProducts_shouldLoadAllDatesAndStores() {
        List<Product> products = service.loadAllHistoricalProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());

        boolean hasLidl = products.stream().anyMatch(p -> "Lidl".equals(p.getStore()));
        boolean hasKaufland = products.stream().anyMatch(p -> "Kaufland".equals(p.getStore()));
        boolean hasAltex = products.stream().anyMatch(p -> "Altex".equals(p.getStore()));
        boolean hasEmag = products.stream().anyMatch(p -> "eMAG".equals(p.getStore()));

        assertTrue(hasLidl);
        assertTrue(hasKaufland);
        assertTrue(hasAltex);
        assertTrue(hasEmag);
    }

}
