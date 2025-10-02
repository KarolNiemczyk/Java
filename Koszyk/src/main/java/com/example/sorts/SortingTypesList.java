package com.example.sorts;

import com.example.ProductClass.Product;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortingTypesList {
    private final Map<String, Comparator<Product>> sortTypes = new HashMap<>();

    public SortingTypesList() {
        addSortType(new SortByPriceAsc());
        addSortType(new SortByPriceDesc());
        addSortType(new SortByNameAsc());
        addSortType(new SortByNameDesc());
        addSortType(new SortByPriceThenNameDesc());
    }

    public Map<String, Comparator<Product>> getSortTypes() {
        return this.sortTypes;
    }

    public void addSortType(Sort sortType) {
        this.sortTypes.put(sortType.getName(), sortType.getComparator());
    }
}