package com.market.pricecomparator.controller;

import com.market.pricecomparator.model.Product;
import com.market.pricecomparator.service.CsvProductService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CsvProductService csvProductService;

    public ProductController(CsvProductService csvProductService) {
        this.csvProductService = csvProductService;
    }

    @GetMapping("/compare/value-per-unit")
    public List<Product> getBestValuePerUnit() {
        List<Product> all = new ArrayList<>();

        all.addAll(csvProductService.loadStandardProductsFromCsv("lidl_2025-05-08.csv", "Lidl"));
        all.addAll(csvProductService.loadStandardProductsFromCsv("profi_2025-05-08.csv", "Profi"));
        all.addAll(csvProductService.loadStandardProductsFromCsv("kaufland_2025-05-08.csv", "Kaufland"));

        return all.stream()
                .collect(Collectors.groupingBy(Product::getProductId))
                .values().stream()
                .map(group -> group.stream()
                        .min(Comparator.comparingDouble(Product::getPricePerUnit))
                        .get())
                .toList();
    }
}
