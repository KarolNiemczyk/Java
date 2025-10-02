package com.example.model;

import com.example.observer.Observable;
import com.example.observer.Observer;
import com.example.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Buyer implements Observer, Observable {
    private List<Observer> observers = new ArrayList<>();
    private double budget;
    private final double initialBudget;
    private int necessityNeeds;
    private double inflation;
    private List<Product> marketProducts = new ArrayList<>();
    private CentralBank centralBank;

    public Buyer(double budget, int necessityNeeds, CentralBank centralBank) {
        this.budget = budget;
        this.initialBudget = budget;
        this.necessityNeeds = necessityNeeds;
        this.centralBank = centralBank;
    }

    @Override
    public void update() {
        this.inflation = centralBank.getInflation();
    }

    private boolean shouldSkipPurchase(Product product) {
        double price = product.getPrice() * (1 + inflation / 100);
        if (product.getType() == ProductType.NECESSITY && necessityNeeds > 0) {
            return price > budget * 0.75; // Wyższy próg dla koniecznych
        }
        return price > budget * 0.5; // Standardowy dla luksusowych
    }
    public void setInflation(double inflation) {
        this.inflation = inflation;
    }

    public double getInflation() {
        return inflation;
    }

    public void observeMarket(List<Product> products) {
        this.marketProducts = products;
    }

    public void makePurchases() {
        double totalSpent = 0;

        // First, process NECESSITY products
        for (Product product : marketProducts) {
            if (product.getStock() == 0) continue;
            if (product.getType() == ProductType.NECESSITY && necessityNeeds > 0) {
                if (shouldSkipPurchase(product)) continue; // Pomijaj zbyt drogie
                double price = product.getPrice();
                double affordability = budget / (price * (1 + inflation / 100));
                int toBuy = Math.min(necessityNeeds, Math.min(product.getStock(), (int) affordability));
                if (toBuy > 0 && price * toBuy <= budget) {
                    product.reduceStock(toBuy);
                    budget -= price * toBuy;
                    totalSpent += price * toBuy;
                    necessityNeeds -= toBuy;
                    notifyObservers();
                }
            }
        }

        // Then, process LUXURY products if no necessity needs remain
        if (necessityNeeds == 0) {
            for (Product product : marketProducts) {
                if (product.getStock() == 0) continue;
                if (product.getType() == ProductType.LUXURY) {
                    if (shouldSkipPurchase(product)) continue; // Pomijaj zbyt drogie
                    double price = product.getPrice();
                    double affordability = budget / (price * (1 + inflation / 100));
                    int toBuy = Math.min(1, Math.min(product.getStock(), (int) affordability));
                    if (toBuy > 0 && price * toBuy <= budget) {
                        product.reduceStock(toBuy);
                        budget -= price * toBuy;
                        totalSpent += price * toBuy;
                        notifyObservers();
                    }
                }
            }
        }
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) obs.update();
    }

    public double getBudget() {
        return budget;
    }

    public double getInitialBudget() {
        return initialBudget;
    }

    public int getNecessityNeeds() {
        return necessityNeeds;
    }

    public void accept(Visitor visitor) {
        visitor.visitBuyer(this);
    }
}