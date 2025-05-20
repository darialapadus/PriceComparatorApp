package com.market.pricecomparator.controller;

import com.market.pricecomparator.model.Product;
import com.market.pricecomparator.service.CsvProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CsvProductService csvProductService;

    public ProductController(CsvProductService csvProductService) {
        this.csvProductService = csvProductService;
    }

    @GetMapping("/load")
    public List<Product> loadProducts(@RequestParam String file) {
        return csvProductService.loadProductsFromCsv(file);
    }
}
