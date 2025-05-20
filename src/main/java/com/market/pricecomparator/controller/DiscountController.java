package com.market.pricecomparator.controller;

import com.market.pricecomparator.model.Discount;
import com.market.pricecomparator.service.CsvDiscountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final CsvDiscountService discountService;

    public DiscountController(CsvDiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/top")
    public List<Discount> getTopDiscounts(@RequestParam(defaultValue = "5") int limit) {
        return discountService.getTopDiscounts(limit);
    }

    @GetMapping("/new")
    public List<Discount> getNewDiscounts() {
        return discountService.getNewDiscounts();
    }

}
