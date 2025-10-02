
package com.example;

import com.example.observer.Observer;
import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {

    private Product necessityProduct;
    private Product luxuryProduct;
    private Seller seller;
    private Buyer buyer;
    private CentralBank centralBank;
    private MarketUpdater marketUpdater;
    private Market market;

    @BeforeEach
    void setUp() {
        necessityProduct = new Product(ProductType.NECESSITY, 10.0, 100);
        luxuryProduct = new Product(ProductType.LUXURY, 50.0, 50);
        centralBank = new CentralBank(100.0);
        seller = new Seller(20.0, centralBank);
        buyer = new Buyer(1000.0, 10, centralBank);
        marketUpdater = new MarketUpdater(5.0);
        market = new Market(centralBank);
    }

    // Testy dla klasy Product
    @Test
    void testNecessityProductConstructor() {
        assertEquals(ProductType.NECESSITY, necessityProduct.getType());
        assertEquals(10.0, necessityProduct.getProductionCost());
        assertEquals(10.0, necessityProduct.getPrice());
        assertEquals(100, necessityProduct.getStock());
    }

    @Test
    void testLuxuryProductConstructor() {
        assertEquals(ProductType.LUXURY, luxuryProduct.getType());
        assertEquals(50.0, luxuryProduct.getProductionCost());
        assertEquals(50.0, luxuryProduct.getPrice());
        assertEquals(50, luxuryProduct.getStock());
    }

    @Test
    void testSetPrice() {
        necessityProduct.setPrice(15.0);
        assertEquals(15.0, necessityProduct.getPrice());
        luxuryProduct.setPrice(75.0);
        assertEquals(75.0, luxuryProduct.getPrice());
    }

    @Test
    void testReduceStock() {
        necessityProduct.reduceStock(50);
        assertEquals(50, necessityProduct.getStock());
        necessityProduct.reduceStock(60);
        assertEquals(50, necessityProduct.getStock());

        luxuryProduct.reduceStock(20);
        assertEquals(30, luxuryProduct.getStock());
        luxuryProduct.reduceStock(40);
        assertEquals(30, luxuryProduct.getStock());
    }

    @Test
    void testNecessityProductAccept() {
        necessityProduct.accept(marketUpdater);
        assertEquals(10.0 * 1.05, necessityProduct.getPrice(), 0.1);
    }

    @Test
    void testLuxuryProductAccept() {
        luxuryProduct.accept(marketUpdater);
        assertEquals(50.0 * 1.05, luxuryProduct.getPrice(), 0.1);
    }

    // Testy dla klasy Seller
    @Test
    void testSellerConstructor() {
        assertEquals(20.0, seller.getMargin(), 0.01);
        assertEquals(0.1, seller.getInflation(), 0.01); // Initial inflation from CentralBank
    }

    @Test
    void testAddProduct() {
        seller.addProduct(necessityProduct);
        assertEquals(1, seller.getProducts().size());
        assertEquals(necessityProduct, seller.getProducts().get(0));

        seller.addProduct(luxuryProduct);
        assertEquals(2, seller.getProducts().size());
        assertEquals(luxuryProduct, seller.getProducts().get(1));
    }

    @Test
    void testUpdatePrices() {
        seller.addProduct(necessityProduct);
        seller.addProduct(luxuryProduct);
        seller.updatePrices();
        // Margin=20%, inflation=0.1%, so 1 + min(0.1/200, 0.025) = 1.0005
        assertEquals(10.0 * 1.20 * 1.0005, necessityProduct.getPrice(), 0.01);
        assertEquals(50.0 * 1.20 * 1.0005, luxuryProduct.getPrice(), 0.01);
    }

    @Test
    void testSellerNotifyObservers() {
        TestObserver observer = new TestObserver();
        seller.addObserver(observer);
        seller.notifyObservers();
        assertTrue(observer.isUpdated());
    }

    @Test
    void testSellerAdjustMargin() {
        seller.addProduct(necessityProduct);
        // High demand: sold 90/100 (above 0.85)
        seller.adjustMargin(90, 100);
        assertEquals(20.1, seller.getMargin(), 0.10);

        // Zero stock: no change
        seller = new Seller(20.0, centralBank); // Reset
        seller.addProduct(necessityProduct);
        seller.adjustMargin(10, 0);
        assertEquals(20.0, seller.getMargin(), 0.01);
    }

    @Test
    void testSellerUpdateInflation() {
        centralBank.setLastTurnover(50.0);
        centralBank.update();
        seller.update();
        assertEquals(0.19, seller.getInflation(), 0.01);
    }

    // Testy dla klasy Buyer
    @Test
    void testBuyerConstructor() {
        assertEquals(1000.0, buyer.getBudget(), 0.01);
        assertEquals(1000.0, buyer.getInitialBudget(), 0.01);
        assertEquals(10, buyer.getNecessityNeeds());
        assertEquals(0.0, buyer.getInflation(), 0.01);
    }

    @Test
    void testSetInflation() {
        buyer.setInflation(5.0);
        assertEquals(5.0, buyer.getInflation(), 0.01);
    }

    @Test
    void testBuyerUpdate() {
        centralBank.setLastTurnover(50.0);
        centralBank.update();
        buyer.update();
        assertEquals(centralBank.getInflation(), buyer.getInflation(), 0.01);
        assertEquals(0.19, buyer.getInflation(), 0.01);
    }

    @Test
    void testBuyerUpdateWithNegativeInflation() {
        centralBank.setLastTurnover(2000.0);
        centralBank.update();
        buyer.update();
        assertEquals(0.09, buyer.getInflation(), 0.01);
    }

    @Test
    void testMakePurchasesWithNecessityProduct() {
        seller.addProduct(necessityProduct);
        necessityProduct.setPrice(10.0);
        buyer.observeMarket(seller.getProducts());
        buyer.makePurchases();
        assertEquals(0, buyer.getNecessityNeeds());
        assertEquals(1000.0 - 10 * 10.0, buyer.getBudget(), 0.01);
        assertEquals(90, necessityProduct.getStock());
    }

    @Test
    void testMakePurchasesWithLuxuryProductOnly() {
        seller.addProduct(luxuryProduct);
        luxuryProduct.setPrice(50.0);
        buyer.observeMarket(seller.getProducts());
        buyer.makePurchases();
        assertEquals(10, buyer.getNecessityNeeds());
        assertEquals(1000.0, buyer.getBudget(), 0.01);
        assertEquals(50, luxuryProduct.getStock());
    }

    @Test
    void testMakePurchasesWithLuxuryProductAfterNecessity() {
        seller.addProduct(necessityProduct);
        seller.addProduct(luxuryProduct);
        necessityProduct.setPrice(10.0);
        luxuryProduct.setPrice(50.0);
        buyer.observeMarket(seller.getProducts());
        buyer.makePurchases();
        assertEquals(0, buyer.getNecessityNeeds());
        assertEquals(1000.0 - 10 * 10.0 - 50.0, buyer.getBudget(), 0.01);
        assertEquals(90, necessityProduct.getStock());
        assertEquals(49, luxuryProduct.getStock());
    }

    @Test
    void testMakePurchasesLuxuryWithNoNecessityNeeds() {
        Buyer luxuryBuyer = new Buyer(1000.0, 0, centralBank);
        seller.addProduct(luxuryProduct);
        luxuryProduct.setPrice(50.0);
        luxuryBuyer.observeMarket(seller.getProducts());
        luxuryBuyer.makePurchases();
        assertEquals(0, luxuryBuyer.getNecessityNeeds());
        assertEquals(1000.0 - 50.0, luxuryBuyer.getBudget(), 0.01);
        assertEquals(49, luxuryProduct.getStock());
    }

    @Test
    void testMakePurchasesLuxuryWithInsufficientBudget() {
        Buyer luxuryBuyer = new Buyer(20.0, 0, centralBank);
        seller.addProduct(luxuryProduct);
        luxuryProduct.setPrice(50.0);
        luxuryBuyer.observeMarket(seller.getProducts());
        luxuryBuyer.makePurchases();
        assertEquals(0, luxuryBuyer.getNecessityNeeds());
        assertEquals(20.0, luxuryBuyer.getBudget(), 0.01);
        assertEquals(50, luxuryProduct.getStock());
    }

    @Test
    void testBuyerSkipPurchaseHighPrice() {
        seller.addProduct(necessityProduct);
        necessityProduct.setPrice(900.0); // >50% budget (1000 * 0.5)
        buyer.observeMarket(seller.getProducts());
        buyer.makePurchases();
        assertEquals(10, buyer.getNecessityNeeds());
        assertEquals(1000.0, buyer.getBudget(), 0.01);
        assertEquals(100, necessityProduct.getStock());
    }

    @Test
    void testBuyerAccept() {
        buyer.accept(marketUpdater);
        assertEquals(5.0, buyer.getInflation(), 0.01);
    }

    @Test
    void testBuyerNotifyObservers() {
        TestObserver observer = new TestObserver();
        buyer.addObserver(observer);
        buyer.notifyObservers();
        assertTrue(observer.isUpdated());
    }

    // Testy dla klasy CentralBank
    @Test
    void testCentralBankConstructor() {
        assertEquals(100.0, centralBank.getTargetTaxRevenue(), 0.01);
        assertEquals(0.1, centralBank.getInflation(), 0.01);
    }

    @Test
    void testSetLastTurnover() {
        centralBank.setLastTurnover(200.0);
        assertEquals(200.0, centralBank.getLastTurnover(), 0.01);
    }

    @Test
    void testAdjustInflationIncrease() {
        centralBank.setLastTurnover(50.0);
        centralBank.update();
        assertEquals(0.20, centralBank.getInflation(), 0.01); // Moves toward 2%
    }

    @Test
    void testAdjustInflationDecrease() {
        centralBank.setLastTurnover(2000.0);
        centralBank.update();
        assertEquals(0.09, centralBank.getInflation(), 0.01);
    }

    @Test
    void testMarketUpdaterConstructor() {
        assertEquals(5.0, marketUpdater.getInflation(), 0.01);
    }

    @Test
    void testVisitProduct() {
        marketUpdater.visitProduct(necessityProduct);
        assertEquals(10.0 * 1.05, necessityProduct.getPrice(), 0.1);
    }

    @Test
    void testVisitBuyer() {
        marketUpdater.visitBuyer(buyer);
        assertEquals(5.0, buyer.getInflation(), 0.01);
    }

    // Testy dla klasy Market
    @Test
    void testMarketConstructor() {
        assertEquals(centralBank, market.getCentralBank());
    }

    @Test
    void testAddSeller() {
        market.addSeller(seller);
        assertEquals(1, market.getSellers().size());
        assertEquals(seller, market.getSellers().get(0));
    }

    @Test
    void testAddBuyer() {
        market.addBuyer(buyer);
        assertEquals(1, market.getBuyers().size());
        assertEquals(buyer, market.getBuyers().get(0));
    }

    @Test
    void testSimulateTurn() {
        seller.addProduct(necessityProduct);
        necessityProduct.setPrice(10.0);
        market.addSeller(seller);
        market.addBuyer(buyer);
        market.simulateTurn();
        assertTrue(buyer.getNecessityNeeds() < 10);
        assertTrue(necessityProduct.getStock() < 100);
        assertTrue(centralBank.getInflation() > 0.1); // Moves toward 2%
    }

    @Test
    void testCalculateTurnover() {
        seller.addProduct(necessityProduct);
        necessityProduct.setPrice(10.0);
        market.addSeller(seller);
        market.addBuyer(buyer);
        buyer.observeMarket(seller.getProducts());
        buyer.makePurchases();
        double turnover = market.calculateTurnover();
        assertEquals(10 * 10.0, turnover, 0.01);
    }

    @Test
    void testCalculateTurnoverWithDifferentInitialBudgets() {
        Buyer buyer1 = new Buyer(500.0, 5, centralBank);
        Buyer buyer2 = new Buyer(2000.0, 10, centralBank);
        seller.addProduct(necessityProduct);
        necessityProduct.setPrice(10.0);
        market.addSeller(seller);
        market.addBuyer(buyer1);
        market.addBuyer(buyer2);
        buyer1.observeMarket(seller.getProducts());
        buyer2.observeMarket(seller.getProducts());
        buyer1.makePurchases(); // Buys 5 units (50.0)
        buyer2.makePurchases(); // Buys 10 units (100.0)
        double turnover = market.calculateTurnover();
        assertEquals(50.0 + 100.0, turnover, 0.01);
    }

}

// Klasa pomocnicza do testowania obserwatorów
class TestObserver implements Observer {
    private boolean updated = false;

    @Override
    public void update() {
        updated = true;
    }

    public boolean isUpdated() {
        return updated;
    }
}
