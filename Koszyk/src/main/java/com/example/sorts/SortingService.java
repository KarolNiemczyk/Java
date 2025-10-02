package com.example.sorts;

import com.example.ProductClass.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortingService {
    private final Map<String, Comparator<Product>> sortTypes;

    public SortingService(Map<String, Comparator<Product>> sortTypes) {
        this.sortTypes = sortTypes;
    }

    public void sortProductsByType(String sortType, List<Product> products) {
        Comparator<Product> comparator = sortTypes.get(sortType);
        if (comparator != null) {
            products.sort(comparator);
        } else {
            throw new IllegalArgumentException("Sort type not found: " + sortType);
        }
    }
}