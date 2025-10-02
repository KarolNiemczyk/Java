package com.example.sorts;
import com.example.ProductClass.Product;
import java.util.Comparator;

public class SortByPriceThenNameDesc implements Sort {
    @Override
    public String getName() {
        return "Price Then Name Descending";
    }

    @Override
    public Comparator<Product> getComparator() {
        return (p1, p2) -> {
            int priceCompare = Double.compare(p2.getFinalPrice(), p1.getFinalPrice()); // Descending
            if (priceCompare != 0) return priceCompare;
            return p2.getName().compareTo(p1.getName()); // Descending
        };
    }
}