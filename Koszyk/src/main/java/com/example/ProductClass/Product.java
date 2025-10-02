package com.example.ProductClass;

public class Product implements Cloneable {
    public String code;
    public String name;
    public double price;
    private double discountPrice;
    private double originalPrice;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.discountPrice = price;
        this.originalPrice = price;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void applyDiscount(double newPrice) {
        this.discountPrice = newPrice;
    }

    public void applyAdditionalDiscount(double discountRate) {
        this.discountPrice *= (1 - discountRate);
    }

    public double getFinalPrice() {
        return discountPrice;
    }

    @Override
    public Product clone() {
        try {
            Product cloned = (Product) super.clone();
            cloned.originalPrice = this.originalPrice;
            cloned.discountPrice = this.discountPrice;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}