package com.market.pricecomparator.service;

import com.market.pricecomparator.model.Product;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class CsvProductService {

    public List<Product> loadStandardProductsFromCsv(String fileName, String store) {
        List<Product> products = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/" + fileName)) {
            if (is == null) throw new FileNotFoundException("CSV file not found: " + fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length >= 8) {
                    Product p = new Product();
                    p.setProductId(tokens[0]);
                    p.setProductName(tokens[1]);
                    p.setProductCategory(tokens[2]);
                    p.setBrand(tokens[3]);
                    p.setPackageQuantity(Double.parseDouble(tokens[4]));
                    p.setPackageUnit(tokens[5]);
                    p.setPrice(Double.parseDouble(tokens[6]));
                    p.setCurrency(tokens[7]);
                    p.setStore(store);
                    p.setDate(fileName);
                    products.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> loadAllHistoricalProducts() {
        List<Product> products = new ArrayList<>();

        products.addAll(loadStandardProductsFromCsv("lidl_2025-05-01.csv", "Lidl"));
        products.addAll(loadStandardProductsFromCsv("lidl_2025-05-08.csv", "Lidl"));
        products.addAll(loadStandardProductsFromCsv("profi_2025-05-01.csv", "Profi"));
        products.addAll(loadStandardProductsFromCsv("profi_2025-05-08.csv", "Profi"));
        products.addAll(loadStandardProductsFromCsv("kaufland_2025-05-01.csv", "Kaufland"));
        products.addAll(loadStandardProductsFromCsv("kaufland_2025-05-08.csv", "Kaufland"));

        return products;
    }

}
