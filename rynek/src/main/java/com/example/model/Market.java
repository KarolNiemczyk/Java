package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Market {
    private List<Seller> sellers = new ArrayList<>();
    private List<Buyer> buyers = new ArrayList<>();
    private CentralBank centralBank;
    private MarketUpdater updater;

    public Market(CentralBank centralBank) {
        this.centralBank = centralBank;
        this.updater = new MarketUpdater(centralBank.getInflation());
    }

    public void addSeller(Seller seller) {
        sellers.add(seller);
        seller.addObserver(centralBank);
    }

    public void addBuyer(Buyer buyer) {
        buyers.add(buyer);
        buyer.addObserver(centralBank);
    }

    public void simulateTurn() {
        // Śledzenie początkowych zapasów
        Map<Product, Integer> initialStocks = new HashMap<>();
        for (Seller seller : sellers) {
            for (Product product : seller.getProducts()) {
                initialStocks.put(product, product.getStock());
            }
        }

        // Aktualizacja cen przez sprzedawców
        for (Seller seller : sellers) {
            seller.updatePrices();
        }

        // Kupujący obserwują rynek i dokonują zakupów
        List<Product> allProducts = sellers.stream()
                .flatMap(s -> s.getProducts().stream())
                .collect(Collectors.toList());
        for (Buyer buyer : buyers) {
            buyer.observeMarket(allProducts);
            buyer.makePurchases();
        }

        // Dostosowanie marży na podstawie sprzedaży
        for (Seller seller : sellers) {
            for (Product product : seller.getProducts()) {
                int initialStock = initialStocks.getOrDefault(product, product.getStock());
                int sold = initialStock - product.getStock();
                seller.adjustMargin(sold, initialStock);
            }
        }

        // Aktualizacja danych za pomocą Visitora
        updater = new MarketUpdater(centralBank.getInflation());
        allProducts.forEach(product -> product.accept(updater));
        buyers.forEach(buyer -> buyer.accept(updater));

        // Obliczanie obrotów i aktualizacja inflacji
        double turnover = calculateTurnover();
        centralBank.setLastTurnover(turnover);
        centralBank.update();
    }

    public double calculateTurnover() {
        return buyers.stream()
                .map(b -> b.getInitialBudget() - b.getBudget())
                .reduce(0.0, Double::sum);
    }

    public CentralBank getCentralBank() {
        return centralBank;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public List<Buyer> getBuyers() {
        return buyers;
    }
}