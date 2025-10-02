package com.example.promotions;

import com.example.shoppingCart.ShoppingCart;

public interface Promotion extends Cloneable {
    void apply(ShoppingCart cart);
    Promotion clone();
}