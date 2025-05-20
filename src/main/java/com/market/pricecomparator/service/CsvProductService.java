package com.market.pricecomparator.service;

import com.market.pricecomparator.model.Product;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class CsvProductService {

    public List<Product> loadProductsFromCsv(String fileName) {
        List<Product> products = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new FileNotFoundException("File not found in resources: " + fileName);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 6) {
                    Product p = new Product(
                            tokens[0],
                            Double.parseDouble(tokens[1]),
                            tokens[2],
                            tokens[3],
                            Double.parseDouble(tokens[4]),
                            tokens[5]
                    );
                    products.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

}
