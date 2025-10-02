package com.example;

import com.example.ProductClass.Product;
import com.example.promotions.*;
import com.example.shoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class ShoppingCartTest {

    @Test
    void testTotalPriceWithoutPromotions() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        assertEquals(450, cart.getTotalPrice(), 0.001);
    }

    @Test
    void testFindCheapestAndMostExpensive() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product p1 = new Product("001", "Laptop", 400);
        Product p2 = new Product("002", "Mouse", 50);
        Product p3 = new Product("003", "Keyboard", 70);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        assertEquals(p2, cart.findCheapest());
        assertEquals(p1, cart.findMostExpensive());
    }

    @Test
    void testGetNCheapestAndNMostExpensive() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product p1 = new Product("001", "Laptop", 400);
        Product p2 = new Product("002", "Mouse", 50);
        Product p3 = new Product("003", "Keyboard", 70);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        Product[] cheapestTwo = cart.getNCheapest(2);
        assertEquals("Mouse", cheapestTwo[0].getName());
        assertEquals("Keyboard", cheapestTwo[1].getName());

        Product[] mostExpensiveTwo = cart.getNMostExpensive(2);
        assertEquals("Laptop", mostExpensiveTwo[0].getName());
        assertEquals("Keyboard", mostExpensiveTwo[1].getName());
    }

    @Test
    void testSortByPrice() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.sortByPrice();
        Product[] sorted = cart.getProducts();
        assertEquals("Mouse", sorted[0].getName());
        assertEquals("Keyboard", sorted[1].getName());
        assertEquals("Laptop", sorted[2].getName());
    }

    @Test
    void testSortByName() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.sortByName();
        Product[] sorted = cart.getProducts();
        assertEquals("Keyboard", sorted[0].getName());
        assertEquals("Laptop", sorted[1].getName());
        assertEquals("Mouse", sorted[2].getName());
    }

    @Test
    void testEnsureProductCapacity() {
        ShoppingCart cart = new ShoppingCart(2, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.addProduct(new Product("004", "Monitor", 300));
        Product[] products = cart.getProducts();
        assertEquals(4, products.length);
        assertEquals("Monitor", products[3].getName());
    }

    @Test
    void testGetNMostExpensiveOrder() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 1000));
        cart.addProduct(new Product("002", "Phone", 600));
        cart.addProduct(new Product("003", "Tablet", 300));
        cart.addProduct(new Product("004", "Mouse", 50));
        cart.addProduct(new Product("005", "Keyboard", 100));
        Product[] top3 = cart.getNMostExpensive(3);
        assertEquals(3, top3.length);
        assertEquals("Laptop", top3[0].getName());
        assertEquals("Phone", top3[1].getName());
        assertEquals("Tablet", top3[2].getName());
    }

    @Test
    void testEnsurePromotionCapacity() {
        ShoppingCart cart = new ShoppingCart(10, 1);
        cart.addPromotion(new FreeMugPromotion());
        cart.addPromotion(new BuyTwoGetOneFreePromotion());
        cart.addPromotion(new PercentageDiscountPromotion(100, 0.1));
        cart.addProduct(new Product("001", "Laptop", 150));
        cart.applyPromotions();
        assertDoesNotThrow(() -> cart.applyPromotions());
    }

    @Test
    void testFindMostExpensiveConditionBranch() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product p1 = new Product("001", "USB Cable", 10);
        Product p2 = new Product("002", "Monitor", 800);
        cart.addProduct(p1);
        cart.addProduct(p2);
        Product result = cart.findMostExpensive();
        assertEquals("Monitor", result.getName());
    }

    @Test
    void testBestPromotionOrder() {
        ShoppingCart cart = new ShoppingCart(10, 5);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.addPromotion(new PercentageDiscountPromotion(300, 0.1));
        cart.addPromotion(new BuyTwoGetOneFreePromotion());
        List<Promotion> bestOrder = cart.getBestPromotionOrder();
        assertFalse(bestOrder.isEmpty(), "Najlepsza kolejność promocji nie powinna być pusta");
    }

    @Test
    void testBestPromotionOrderCalculatesLowestPrice() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.addPromotion(new PercentageDiscountPromotion(300, 0.10));
        cart.addPromotion(new BuyTwoGetOneFreePromotion());
        cart.addPromotion(new CouponPromotion("003"));
        cart.addPromotion(new FreeMugPromotion());
        List<Promotion> bestOrder = cart.getBestPromotionOrder();
        ShoppingCart finalCart = new ShoppingCart(10, 10);
        finalCart.addProduct(new Product("001", "Laptop", 400));
        finalCart.addProduct(new Product("002", "Mouse", 50));
        finalCart.addProduct(new Product("003", "Keyboard", 70));
        for (Promotion promo : bestOrder) {
            finalCart.addPromotion(promo);
        }
        finalCart.applyPromotions();
        double finalPrice = finalCart.getTotalPrice();
        System.out.println("Wybrana kombinacja promocji: " + bestOrder);
        System.out.println("Cena końcowa: " + finalPrice);
        assertTrue(finalPrice <= 404.1, "Cena końcowa powinna być mniejsza lub równa 404.1 zł (po zastosowaniu wszystkich promocji)");
    }

    @Test
    void testEmptyCart() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        assertEquals(0, cart.getTotalPrice(), 0.001, "Cena pustego koszyka powinna wynosić 0");
        assertNull(cart.findCheapest(), "Najtańszy produkt w pustym koszyku powinien być null");
        assertNull(cart.findMostExpensive(), "Najdroższy produkt w pustym koszyku powinien być null");
        assertEquals(0, cart.getNCheapest(5).length, "Lista najtańszych produktów powinna być pusta");
        assertEquals(0, cart.getNMostExpensive(5).length, "Lista najdroższych produktów powinna być pusta");
    }

    @Test
    void testInvalidSortType() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        assertThrows(IllegalArgumentException.class, () -> cart.sort("Invalid Sort"),
                "Nieprawidłowy typ sortowania powinien rzucić wyjątek");
    }

    @Test
    void testSortByPriceThenNameDesc() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 400));
        cart.addProduct(new Product("003", "Keyboard", 70));
        cart.sort("Price Then Name Descending");
        Product[] sorted = cart.getProducts();
        assertEquals("Mouse", sorted[0].getName(), "Mouse powinien być pierwszy (cena 400, nazwa malejąco)");
        assertEquals("Laptop", sorted[1].getName(), "Laptop powinien być drugi (cena 400, nazwa malejąco)");
        assertEquals("Keyboard", sorted[2].getName(), "Keyboard powinien być trzeci (cena 70)");
    }

    @Test
    void testPercentageDiscountPromotion() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        cart.addPromotion(new PercentageDiscountPromotion(300, 0.05));
        cart.applyPromotions();
        assertEquals(427.5, cart.getTotalPrice(), 0.001, "Cena po 5% rabacie powinna wynosić 427.5 zł");
    }
}