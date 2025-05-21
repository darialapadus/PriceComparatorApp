package com.market.pricecomparator.service;

import com.market.pricecomparator.model.Discount;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvDiscountService {

    public List<Discount> loadDiscountsFromCsv(String fileName, String store) {
        List<Discount> discounts = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/" + fileName)) {
            if (is == null) throw new FileNotFoundException("Discount file not found: " + fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length >= 9) {
                    Discount d = new Discount();
                    d.setProductId(tokens[0]);
                    d.setProductName(tokens[1]);
                    d.setBrand(tokens[2]);
                    d.setQuantity(Double.parseDouble(tokens[3]));
                    d.setUnit(tokens[4]);
                    d.setCategory(tokens[5]);
                    d.setFromDate(tokens[6]);
                    d.setToDate(tokens[7]);
                    d.setPercentage(Double.parseDouble(tokens[8]));
                    d.setStore(store);
                    discounts.add(d);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return discounts;
    }

    public List<Discount> getTopDiscounts(int limit) {
        List<Discount> all = new ArrayList<>();
        all.addAll(loadDiscountsFromCsv("lidl_discounts_2025-05-08.csv", "Lidl"));
        all.addAll(loadDiscountsFromCsv("profi_discounts_2025-05-08.csv", "Profi"));
        all.addAll(loadDiscountsFromCsv("kaufland_discounts_2025-05-08.csv", "Kaufland"));

        all.addAll(loadDiscountsFromCsv("altex_discounts_2025-05-21.csv", "Altex"));
        all.addAll(loadDiscountsFromCsv("emag_discounts_2025-05-21.csv", "eMAG"));

        return all.stream()
                .sorted((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()))
                .limit(limit)
                .toList();
    }

    public List<Discount> getNewDiscounts() {
        List<Discount> oldDiscounts = new ArrayList<>();
        List<Discount> newDiscounts = new ArrayList<>();

        oldDiscounts.addAll(loadDiscountsFromCsv("lidl_discounts_2025-05-01.csv", "Lidl"));
        oldDiscounts.addAll(loadDiscountsFromCsv("profi_discounts_2025-05-01.csv", "Profi"));
        oldDiscounts.addAll(loadDiscountsFromCsv("kaufland_discounts_2025-05-01.csv", "Kaufland"));

        newDiscounts.addAll(loadDiscountsFromCsv("lidl_discounts_2025-05-08.csv", "Lidl"));
        newDiscounts.addAll(loadDiscountsFromCsv("profi_discounts_2025-05-08.csv", "Profi"));
        newDiscounts.addAll(loadDiscountsFromCsv("kaufland_discounts_2025-05-08.csv", "Kaufland"));

        oldDiscounts.addAll(loadDiscountsFromCsv("altex_discounts_2025-05-01.csv", "Altex"));
        oldDiscounts.addAll(loadDiscountsFromCsv("emag_discounts_2025-05-01.csv", "eMAG"));

        newDiscounts.addAll(loadDiscountsFromCsv("altex_discounts_2025-05-21.csv", "Altex"));
        newDiscounts.addAll(loadDiscountsFromCsv("emag_discounts_2025-05-21.csv", "eMAG"));


        Set<String> oldKeys = oldDiscounts.stream()
                .map(d -> d.getProductId() + "_" + d.getStore())
                .collect(Collectors.toSet());

        return newDiscounts.stream()
                .filter(d -> !oldKeys.contains(d.getProductId() + "_" + d.getStore()))
                .toList();
    }

}
