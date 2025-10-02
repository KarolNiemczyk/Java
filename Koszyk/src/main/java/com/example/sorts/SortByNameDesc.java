package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;

public class SortByNameDesc implements Sort {
    @Override
    public String getName() {
        return "Name Descending";
    }

    @Override
    public Comparator<Product> getComparator() {
        return Comparator.comparing(Product::getName).reversed();
    }
}