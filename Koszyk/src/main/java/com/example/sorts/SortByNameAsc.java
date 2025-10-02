package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;

public class SortByNameAsc implements Sort {
    @Override
    public String getName() {
        return "Name Ascending";
    }

    @Override
    public Comparator<Product> getComparator() {
        return Comparator.comparing(Product::getName);
    }
}