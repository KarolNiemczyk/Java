package com.example;

import com.example.ProductClass.Product;
import com.example.promotions.BuyTwoGetOneFreePromotion;
import com.example.promotions.CouponPromotion;
import com.example.promotions.FreeMugPromotion;
import com.example.promotions.PercentageDiscountPromotion;
import com.example.shoppingCart.ShoppingCart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PromotionsTest {

    @Test
    void testPercentageDiscountPromotion() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        // Przy łącznej wartości 450 zł, przy progu 300 zł i rabacie 5%
        cart.addPromotion(new PercentageDiscountPromotion(300, 0.05));
        cart.applyPromotions();
        // Nowa cena: Laptop 400*0.95 = 380, Mouse 50*0.95 = 47.5 => suma = 427.5 zł
        assertEquals(427.5, cart.getTotalPrice(), 0.001);
    }
    @Test
    void testPercentageDiscountPromotionFail() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 400));
        cart.addProduct(new Product("002", "Mouse", 50));
        // Przy łącznej wartości 450 zł, przy progu 300 zł i rabacie 5%
        cart.addPromotion(new PercentageDiscountPromotion(1000, 0.05));
        cart.applyPromotions();
        assertEquals(450, cart.getTotalPrice(), 0.001);
    }

    @Test
    void testBuyTwoGetOneFreePromotion() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product p1 = new Product("001", "Laptop", 400);
        Product p2 = new Product("002", "Mouse", 50);
        Product p3 = new Product("003", "Keyboard", 70);
        assertEquals(50,p2.getPrice());
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.addPromotion(new BuyTwoGetOneFreePromotion());
        cart.applyPromotions();
        // W promocji najtańszy produkt (Mouse, 50 zł) powinien być darmowy: 400 + 0 + 70 = 470 zł
        assertEquals(470, cart.getTotalPrice(), 0.001);
    }


    @Test
    void testFreeMugPromotion() {
        // Przechwycenie wyjścia standardowego, aby sprawdzić komunikat
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        ShoppingCart cart = new ShoppingCart(10, 10);
        cart.addProduct(new Product("001", "Laptop", 150));
        cart.addProduct(new Product("002", "Mouse", 60));
        // Łączna wartość = 210 zł, >200 zł, więc powinien pojawić się komunikat
        cart.addPromotion(new FreeMugPromotion());
        cart.applyPromotions();

        String output = outContent.toString();
        assertTrue(output.contains("Darmowy firmowy kubek"));

        // Przywrócenie oryginalnego System.out
        System.setOut(originalOut);
    }

    @Test
    void testCouponPromotion() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product p1 = new Product("001", "Laptop", 400);
        Product p2 = new Product("002", "Mouse", 50);
        cart.addProduct(p1);
        cart.addProduct(p2);
        CouponPromotion coupon = new CouponPromotion("002");
        cart.addPromotion(coupon);
        // Pierwsze wywołanie promocji powinno zastosować kupon do produktu "Mouse"
        cart.applyPromotions();
        double expectedMousePrice = 50 * 0.70; // 30% rabatu
        assertEquals(expectedMousePrice, p2.getFinalPrice(), 0.001);
        // Kolejne wywołanie promocji nie powinno zmienić ceny, bo kupon jest jednorazowy
        cart.applyPromotions();
        assertEquals(expectedMousePrice, p2.getFinalPrice(), 0.001);
    }
    @Test
    void testCouponPromotionFailWhenProductNotInCart() {
        ShoppingCart cart = new ShoppingCart(10, 10);
        Product laptop = new Product("001", "Laptop", 3000);
        cart.addProduct(laptop);

        double originalPrice = laptop.getPrice();

        CouponPromotion coupon = new CouponPromotion("999"); // kod, którego nie ma w koszyku
        cart.addPromotion(coupon);
        cart.applyPromotions();

        assertEquals(originalPrice, laptop.getFinalPrice(), 0.001, "Kupon nie powinien być zastosowany");
    }

}
