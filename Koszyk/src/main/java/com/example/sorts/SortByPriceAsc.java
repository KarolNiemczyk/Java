package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;

public class SortByPriceAsc implements Sort {
    @Override
    public String getName() {
        return "Price Ascending";
    }

    @Override
    public Comparator<Product> getComparator() {
        return Comparator.comparingDouble(Product::getFinalPrice); // Changed to getFinalPrice for consistency
    }
}