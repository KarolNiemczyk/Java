package com.example.promotions;

import com.example.ProductClass.Product;
import com.example.shoppingCart.ShoppingCart;

public class CouponPromotion implements Promotion {
    private final String productCode;
    private boolean used;

    public CouponPromotion(String productCode) {
        this.productCode = productCode;
        this.used = false;
    }

    @Override
    public void apply(ShoppingCart cart) {
        if (used) {
            System.out.println("Kupon już użyty, pomijanie.");
            return;
        }

        for (Product product : cart.getProducts()) {
            if (product.getCode().equals(productCode)) {
                double currentPrice = product.getFinalPrice();
                product.applyAdditionalDiscount(0.30);
                used = true;
                System.out.println("Zastosowano kupon rabatowy na produkt: " + product.getName() +
                        ", cena z " + currentPrice + " na " + product.getFinalPrice());
                break;
            }
        }
    }

    @Override
    public Promotion clone() {
        return new CouponPromotion(this.productCode); // Nowa instancja z resetowanym used
    }
}