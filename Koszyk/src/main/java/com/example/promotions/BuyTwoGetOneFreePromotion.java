package com.example.promotions;

import com.example.ProductClass.Product;
import com.example.shoppingCart.ShoppingCart;

import java.util.Arrays;
import java.util.Comparator;

public class BuyTwoGetOneFreePromotion implements Promotion {
    @Override
    public void apply(ShoppingCart cart) {
        Product[] products = cart.getProducts();
        int size = products.length;

        if (size >= 3) {
            // Sortujemy kopię produktów rosnąco wg ceny
            Product[] sorted = Arrays.copyOf(products, size);
            Arrays.sort(sorted, Comparator.comparingDouble(Product::getFinalPrice));
            Product cheapest = sorted[0];

            for (Product product : products) {
                if (product == cheapest) {
                    product.applyDiscount(0);
                    System.out.println("Kup 2, trzeci gratis! " + product.getName() + " za 0 zł.");
                    break;
                }
            }
        } else {
            System.out.println("Nie spełniono warunku promocji 'Kup 2, trzeci gratis'");
        }
    }
    @Override
    public Promotion clone() {
        return new BuyTwoGetOneFreePromotion();
    }

}
