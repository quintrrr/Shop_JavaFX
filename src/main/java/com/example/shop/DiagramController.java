package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.*;

public class DiagramController implements Initializable {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> productNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // productNames будет заполнен снаружи
    }

    public void fillProductNames(List<Product> allProducts) {
        Set<String> uniqueNames = new LinkedHashSet<>();

        for (Product p : allProducts) {
            uniqueNames.add(p.getProductName());
        }

        productNames.addAll(uniqueNames);
        xAxis.setCategories(productNames);
    }

    public void setProductData(List<Purchases> orders) {

        int[] productCounter = new int[productNames.size()];

        for (Purchases order : orders) {
            for (String artikul : order.getProductData()) {

                for (Product p : Purchases.getAllProducts()) {
                    if (String.valueOf(p.getProductId()).equals(artikul)) {

                        int index = productNames.indexOf(p.getProductName());
                        if (index >= 0) {
                            productCounter[index]++;
                        }
                    }
                }
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Продажи товаров");

        for (int i = 0; i < productCounter.length; i++) {
            series.getData().add(
                    new XYChart.Data<>(productNames.get(i), productCounter[i])
            );
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }
}
