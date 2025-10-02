package com.example.model;

import com.example.visitor.Visitor;

public class Product {
    private ProductType type;
    private double productionCost;
    private double price;
    private int stock;

    public Product(ProductType type, double productionCost, int stock) {
        this.type = type;
        this.productionCost = productionCost;
        this.price = productionCost;
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public ProductType getType() {
        return type;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int amount) {
        if (amount <= stock) stock -= amount;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public void accept(Visitor visitor) {
        visitor.visitProduct(this);
    }
}