package com.example.shoppingCart;

import com.example.ProductClass.Product;
import com.example.promotions.Promotion;
import com.example.sorts.SortingService;
import com.example.sorts.SortingTypesList;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {
    private List<Product> products;
    private List<Promotion> promotions;
    private SortingService sortingService;

    public ShoppingCart(int productCapacity, int promoCapacity) {
        this(productCapacity, promoCapacity, new SortingService(new SortingTypesList().getSortTypes()));
    }

    public ShoppingCart(int productCapacity, int promoCapacity, SortingService sortingService) {
        products = new ArrayList<>(productCapacity);
        promotions = new ArrayList<>(promoCapacity);
        this.sortingService = sortingService;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public void applyPromotions() {
        for (Promotion promo : promotions) {
            promo.apply(this);
        }
    }

    public double getTotalPrice() {
        return products.stream().mapToDouble(Product::getFinalPrice).sum();
    }

    public void sort(String sortType) {
        sortingService.sortProductsByType(sortType, products);
    }

    public void sortByPrice() {
        sort("Price Ascending");
    }

    public void sortByName() {
        sort("Name Ascending");
    }

    public Product findCheapest() {
        return products.stream().min(Comparator.comparingDouble(Product::getFinalPrice)).orElse(null);
    }

    public Product findMostExpensive() {
        return products.stream().max(Comparator.comparingDouble(Product::getFinalPrice)).orElse(null);
    }

    public Product[] getNCheapest(int n) {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getFinalPrice))
                .limit(n)
                .toArray(Product[]::new);
    }

    public Product[] getNMostExpensive(int n) {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getFinalPrice).reversed())
                .limit(n)
                .toArray(Product[]::new);
    }

    public Product[] getProducts() {
        return products.toArray(new Product[0]);
    }

    public List<Promotion> getBestPromotionOrder() {
        double bestPrice = Double.MAX_VALUE;
        List<Promotion> bestOrder = new ArrayList<>();

        List<List<Promotion>> allCombinations = getAllPromotionCombinations(promotions);

        for (List<Promotion> combination : allCombinations) {
            List<Product> productCopies = products.stream()
                    .map(Product::clone)
                    .collect(Collectors.toList());

            ShoppingCart tempCart = new ShoppingCart(productCopies.size(), combination.size(), sortingService);
            productCopies.forEach(tempCart::addProduct);

            List<Promotion> clonedPromotions = combination.stream()
                    .map(Promotion::clone)
                    .collect(Collectors.toList());

            clonedPromotions.forEach(tempCart::addPromotion);
            tempCart.applyPromotions();
            double priceAfterPromotions = tempCart.getTotalPrice();

            System.out.println("Testowana kombinacja: " + clonedPromotions);
            System.out.println("Cena po promocjach: " + priceAfterPromotions);

            if (priceAfterPromotions < bestPrice) {
                bestPrice = priceAfterPromotions;
                // Tworzymy nową listę z nowo sklonowanymi promocjami
                bestOrder = clonedPromotions.stream()
                        .map(Promotion::clone)
                        .collect(Collectors.toList());
            }
        }

        System.out.println("Najlepsza kombinacja: " + bestOrder);
        System.out.println("Najlepsza cena: " + bestPrice);
        return bestOrder;
    }

    private List<List<Promotion>> getAllPromotionCombinations(List<Promotion> promotions) {
        List<List<Promotion>> allCombos = new ArrayList<>();
        int n = promotions.size();

        for (int i = 1; i < (1 << n); i++) {
            List<Promotion> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(promotions.get(j).clone());
                }
            }
            allCombos.addAll(getPermutations(subset));
        }

        return allCombos;
    }

    private List<List<Promotion>> getPermutations(List<Promotion> list) {
        List<List<Promotion>> result = new ArrayList<>();
        permute(list, 0, result);
        return result;
    }

    private void permute(List<Promotion> list, int start, List<List<Promotion>> result) {
        if (start == list.size()) {
            if (!list.isEmpty()) {
                result.add(new ArrayList<>(list));
            }
            return;
        }

        for (int i = start; i < list.size(); i++) {
            Collections.swap(list, i, start);
            permute(list, start + 1, result);
            Collections.swap(list, i, start);
        }
    }
}