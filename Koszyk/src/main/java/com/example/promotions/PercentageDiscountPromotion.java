package com.example.promotions;

import com.example.ProductClass.Product;
import com.example.shoppingCart.ShoppingCart;

public class PercentageDiscountPromotion implements Promotion {
    private final double threshold;
    private final double discountRate;

    public PercentageDiscountPromotion(double threshold, double discountRate) {
        this.threshold = threshold;
        this.discountRate = discountRate;
    }

    @Override
    public void apply(ShoppingCart cart) {
        double total = cart.getTotalPrice();
        if (total > threshold) {
            for (Product product : cart.getProducts()) {
                product.applyAdditionalDiscount(discountRate);
            }
            System.out.println("Zastosowano " + (int)(discountRate * 100) + "% rabatu przy zamówieniu powyżej " + threshold);
        } else {
            System.out.println("Nie zastosowano rabatu – kwota zamówienia (" + total + ") mniejsza niż próg " + threshold);
        }
    }

    @Override
    public Promotion clone() {
        return new PercentageDiscountPromotion(this.threshold, this.discountRate);
    }
}