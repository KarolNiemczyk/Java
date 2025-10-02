package com.example.promotions;

import com.example.ProductClass.Product;
import com.example.shoppingCart.ShoppingCart;

public class FreeMugPromotion implements Promotion {
    @Override
    public void apply(ShoppingCart cart) {
        double total = cart.getTotalPrice();
        if (total > 200) {
            System.out.println("Darmowy firmowy kubek dodany do koszyka!");
            Product mug = new Product("MUG-001", "Firmowy kubek", 0.0);
            mug.applyDiscount(0.0);
            cart.addProduct(mug);
        }
    }

    @Override
    public Promotion clone() {
        return new FreeMugPromotion();
    }
}