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

public class InflationChart {
    public static void main(String[] args) {
        // Inicjalizacja danych
        CentralBank centralBank = new CentralBank(100.0);
        Seller seller = new Seller(10.0, centralBank); // Lower initial margin
        Buyer buyer = new Buyer(5000.0, 50, centralBank); // Higher budget and needs
        Market market = new Market(centralBank);
        Product necessityProduct = new Product(ProductType.NECESSITY, 5.0, 1000); // Lower cost, more stock
        seller.addProduct(necessityProduct);
        market.addSeller(seller);
        market.addBuyer(buyer);

        // Zbieranie danych inflacji
        XYSeries series = new XYSeries("Inflacja (%)");
        for (int turn = 1; turn <= 50; turn++) {
            if (turn == 20) {
                // Mocne zaburzenie: ustawienie inflacji na 15% i marży na 50%
                centralBank.setInflation(15.0);
                seller.setMargin(50.0);
            }
            market.simulateTurn();
            series.add(turn, centralBank.getInflation());
        }

        // Tworzenie wykresu
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Zachowanie inflacji w trakcie 50 tur z zaburzeniem w turze 20", // Tytuł
                "Tura", // Oś X
                "Inflacja (%)", // Oś Y
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
            File outputFile = new File("wykresy/inflation_chart_with_disruptionn.svg");
            SVGUtils.writeToSVG(outputFile, svg2d.getSVGElement());
            System.out.println("Wykres inflacji zapisany jako: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania wykresu: " + e.getMessage());
        }
    }
}