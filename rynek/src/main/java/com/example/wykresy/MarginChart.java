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

public class MarginChart {
    public static void main(String[] args) {
        // Inicjalizacja danych
        CentralBank centralBank = new CentralBank(100.0);
        Seller seller = new Seller(10.0, centralBank);
        Buyer buyer = new Buyer(1000.0, 10, centralBank);
        Market market = new Market(centralBank);
        Product necessityProduct = new Product(ProductType.NECESSITY, 10.0, 100);
        seller.addProduct(necessityProduct);
        market.addSeller(seller);
        market.addBuyer(buyer);

        // Zbieranie danych marży
        XYSeries series = new XYSeries("Marża (%)");
        for (int turn = 1; turn <= 140; turn++) {
            if (turn == 40) {
                // Mocne zaburzenie: ustawienie inflacji na 15% i marży na 50%
                centralBank.setInflation(15.0);
                seller.setMargin(50.0);
            }
            market.simulateTurn();
            series.add(turn, seller.getMargin());
        }

        // Tworzenie wykresu
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Zachowanie marży w trakcie 140 tur z zaburzeniem w turze 40", // Tytuł
                "Tura", // Oś X
                "Marża (%)", // Oś Y
                dataset,
                PlotOrientation.VERTICAL,
                true, // Pokaż legendę
                true, // Tooltips
                false // URLs
        );

        // Zapisywanie wykresu jako SVG
        try {
            SVGGraphics2D svg2d = new SVGGraphics2D(800, 600);
            chart.draw(svg2d, new java.awt.Rectangle(0, 0, 800, 600));
            File outputFile = new File("wykresy/margin_chart_with_disruption.svg");
            SVGUtils.writeToSVG(outputFile, svg2d.getSVGElement());
            System.out.println("Wykres marży zapisany jako: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania wykresu: " + e.getMessage());
        }
    }
}