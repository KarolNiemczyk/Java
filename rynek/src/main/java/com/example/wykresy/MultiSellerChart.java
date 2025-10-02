package com.example.wykresy;

import com.example.model.CentralBank;
import com.example.model.Buyer;
import com.example.model.Seller;
import com.example.model.Market;
import com.example.model.Product;
import com.example.model.ProductType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiSellerChart {
    public static void main(String[] args) {
        // Inicjalizacja danych
        CentralBank centralBank = new CentralBank(100.0);
        Market market = new Market(centralBank);

        // Tworzenie wielu sprzedawców z różnymi produktami
        List<Seller> sellers = new ArrayList<>();
        sellers.add(createSeller(10.0, centralBank, new Product(ProductType.NECESSITY, 10.0, 100), new Product(ProductType.LUXURY, 50.0, 50)));
        sellers.add(createSeller(30.0, centralBank, new Product(ProductType.NECESSITY, 12.0, 80), new Product(ProductType.LUXURY, 60.0, 40)));
        sellers.add(createSeller(40.0, centralBank, new Product(ProductType.NECESSITY, 8.0, 120), new Product(ProductType.LUXURY, 45.0, 60)));

        // Dodanie sprzedawców do rynku
        for (Seller seller : sellers) {
            market.addSeller(seller);
        }

        // Tworzenie kupujących
        Buyer buyer1 = new Buyer(1000.0, 10, centralBank);
        Buyer buyer2 = new Buyer(800.0, 8, centralBank);

        Buyer buyer3 = new Buyer(1500.0, 10, centralBank);
        Buyer buyer4 = new Buyer(800.0, 8, centralBank);
        market.addBuyer(buyer1);
        market.addBuyer(buyer2);
        market.addBuyer(buyer3);
        market.addBuyer(buyer4);

        // Zbieranie danych marży dla wszystkich sprzedawców
        XYSeriesCollection dataset = new XYSeriesCollection();
        List<XYSeries> seriesList = new ArrayList<>();
        for (int i = 0; i < sellers.size(); i++) {
            seriesList.add(new XYSeries("Sprzedawca " + (i + 1)));
        }

        for (int turn = 1; turn <= 140; turn++) {
            if (turn == 40) {
                // Mocne zaburzenie: ustawienie inflacji na 15% i marży na 50% dla wszystkich sprzedawców
                centralBank.setInflation(15.0);
                for (Seller seller : sellers) {
                    seller.setMargin(50.0);
                }
            }
            market.simulateTurn();
            for (int i = 0; i < sellers.size(); i++) {
                seriesList.get(i).add(turn, sellers.get(i).getMargin());
            }
        }

        // Dodanie serii do datasetu
        for (XYSeries series : seriesList) {
            dataset.addSeries(series);
        }

        // Tworzenie wykresu
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Zachowanie marży wszystkich sprzedawców w trakcie 140 tur z zaburzeniem w turze 40",
                "Tura",
                "Marża (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Zapisywanie wykresu jako SVG
        try {
            SVGGraphics2D svg2d = new SVGGraphics2D(800, 600);
            chart.draw(svg2d, new java.awt.Rectangle(0, 0, 800, 600));
            File outputFile = new File("wykresy/multi_seller_margin_chart_with_disruption.svg");
            SVGUtils.writeToSVG(outputFile, svg2d.getSVGElement());
            System.out.println("Wykres marży zapisany jako: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania wykresu: " + e.getMessage());
        }
    }

    private static Seller createSeller(double initialMargin, CentralBank centralBank, Product... products) {
        Seller seller = new Seller(initialMargin, centralBank);
        for (Product product : products) {
            seller.addProduct(product);
        }
        return seller;
    }
}