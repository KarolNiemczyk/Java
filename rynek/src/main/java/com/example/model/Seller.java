package com.example.model;

import com.example.observer.Observable;
import com.example.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Seller implements Observable, Observer {
    private List<Observer> observers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private double margin;
    private double inflation;
    private CentralBank centralBank;
    private final double targetSellRatio = 0.75; // Target 75% of stock sold
    private final double minTargetMargin = 20.0; // Minimum satisfactory margin
    private final double maxTargetMargin = 30.0; // Maximum satisfactory margin

    public Seller(double margin, CentralBank centralBank) {
        this.margin = margin;
        this.centralBank = centralBank;
        this.inflation = centralBank.getInflation();
    }

    @Override
    public void update() {
        this.inflation = centralBank.getInflation();
    }

    public void adjustMargin(int sold, int initialStock) {
        if (initialStock == 0) return;

        double sellRatio = (double) sold / initialStock;
        double ratioDeviation = sellRatio - targetSellRatio;

        // Dynamiczne przesunięcie marginesu zależnie od odchylenia od celu
        double adjustment = 5.0 * ratioDeviation; // tuning: 5.0 jako siła reakcji
        double targetMargin = Math.max(minTargetMargin, Math.min(maxTargetMargin, margin + adjustment));

        // Proporcjonalne zbliżenie się do celu
        if (margin > 0 && targetMargin > 0) {
            double ratio = targetMargin / margin;
            double deviation = Math.abs(ratio - 1.0);
            double adjustmentStrength = Math.min(0.3, 0.5 * deviation);

            margin *= Math.pow(ratio, adjustmentStrength);
        }

        // Clamp to [0, 50]
        margin = Math.max(0.0, Math.min(50.0, margin));
    }



    public void updatePrices() {
        for (Product product : products) {
            double cost = product.getProductionCost();
            // Reduce inflation impact to stabilize prices
            double price = cost * (1 + margin / 100) * (1 + Math.min(inflation / 200, 0.025));
            product.setPrice(price);
        }
        notifyObservers();
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double newMargin) {
        this.margin = newMargin;
    }

    public double getInflation() {
        return inflation;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) obs.update();
    }

    public List<Product> getProducts() {
        return products;
    }
}