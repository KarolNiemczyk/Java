package com.example.model;

import com.example.visitor.Visitor;

public class MarketUpdater implements Visitor {
    private double inflation;

    public MarketUpdater(double inflation) {
        this.inflation = inflation;
    }
    public double getInflation() {
        return inflation;
    }
    @Override
    public void visitProduct(Product product) {
        double cost = product.getProductionCost();
        double currentPrice = product.getPrice();
        product.setPrice(currentPrice * (1 + inflation / 100));
    }

    @Override
    public void visitBuyer(Buyer buyer) {
        buyer.setInflation(inflation);
    }
}