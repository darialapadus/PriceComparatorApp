package com.market.pricecomparator.controller;

import com.market.pricecomparator.dto.PricePointDTO;
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

    @GetMapping("/history/{productId}")
    public Map<String, List<PricePointDTO>> getPriceHistoryByProduct(
            @PathVariable String productId,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand
    ) {
        List<Product> all = csvProductService.loadAllHistoricalProducts();

        return all.stream()
                .filter(p -> p.getProductId().equalsIgnoreCase(productId))
                .filter(p -> store == null || p.getStore().equalsIgnoreCase(store))
                .filter(p -> category == null || p.getProductCategory().equalsIgnoreCase(category))
                .filter(p -> brand == null || p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.groupingBy(
                        Product::getStore,
                        Collectors.mapping(p -> new PricePointDTO(p.getDate(), p.getPrice()), Collectors.toList())
                ));
    }

}
