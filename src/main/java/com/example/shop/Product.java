package com.example.shop;

import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Product {
    private String productName;
    private Integer productId, productCount;
    private Double productSum;

    public Product(String productName, int productId, int productCount, double productSum) {
        this.productName = productName;
        this.productId = productId;
        this.productCount = productCount;
        this.productSum = productSum;
    }

    public Product(){}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Double getProductSum() {
        return productSum;
    }

    public void setProductSum(Double productSum) {
        this.productSum = productSum;
    }

    public static void saveProductsToFiles(ObservableList<Product> products){
        String filePath = "products.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Product product : products) {
                writer.write(
                        product.getProductId() + ";" +
                                product.getProductName() + ";" +
                                product.getProductCount() + ";" +
                                product.getProductSum()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обновлении списка продуктов: " + e.getMessage());
        }
    }

    public static void addProduct(Product product){
        String filePath = "products.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(
                        product.getProductId() + ";" +
                                product.getProductName() + ";" +
                                product.getProductCount() + ";" +
                                product.getProductSum()
                );
                writer.newLine();

        } catch (IOException e) {
            System.err.println("Ошибка при обновлении списка продуктов: " + e.getMessage());
        }
    }
}
