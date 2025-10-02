package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;

public class SortByPriceDesc implements Sort {
    @Override
    public String getName() {
        return "Price Descending";
    }

    @Override
    public Comparator<Product> getComparator() {
        return Comparator.comparingDouble(Product::getFinalPrice).reversed(); // Changed to getFinalPrice
    }
}