
package com.market.pricecomparator.service;

import com.market.pricecomparator.model.Discount;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvDiscountServiceTest {

    private final CsvDiscountService service = new CsvDiscountService();

    @Test
    void testLoadDiscountsFromCsv_validFile_shouldLoadDiscounts() {
        List<Discount> discounts = service.loadDiscountsFromCsv("lidl_discounts_2025-05-08.csv", "Lidl");

        assertNotNull(discounts);
        assertFalse(discounts.isEmpty());
        assertEquals("P001", discounts.get(0).getProductId());
    }

    @Test
    void testLoadDiscountsFromCsv_invalidFile_shouldReturnEmptyList() {
        List<Discount> discounts = service.loadDiscountsFromCsv("invalid_file.csv", "TestStore");

        assertNotNull(discounts);
        assertTrue(discounts.isEmpty());
    }

    @Test
    void testGetTopDiscounts_returnsSortedList() {
        List<Discount> top = service.getTopDiscounts(5);

        assertNotNull(top);
        assertFalse(top.isEmpty());
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).getPercentage() >= top.get(i).getPercentage());
        }
    }

    @Test
    void testGetNewDiscounts_returnsOnlyNewEntries() {
        List<Discount> newOnes = service.getNewDiscounts();

        assertNotNull(newOnes);
        assertFalse(newOnes.isEmpty());
        for (Discount d : newOnes) {
            assertNotNull(d.getProductId());
            assertNotNull(d.getStore());
        }
    }
}
