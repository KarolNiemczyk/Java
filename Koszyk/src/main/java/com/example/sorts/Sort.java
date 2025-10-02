package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;

public interface Sort {
    String getName();
    Comparator<Product> getComparator();
}