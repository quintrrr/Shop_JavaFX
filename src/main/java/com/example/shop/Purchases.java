package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Purchases {
    private Integer id;
    private String login;
    private LocalDate date;
    private String status;

    private ObservableList<String> productData;
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ObservableList<String> getProductData() { return productData; }
    public void setProductData(ObservableList<String> productData) { this.productData = productData; }

    public static ObservableList<Product> getAllProducts() { return allProducts; }
    public static void setAllProducts(ObservableList<Product> list) { allProducts = list; }

    public Purchases(Integer id, String login, LocalDate date, String status, List<String> products) {
        this.id = id;
        this.login = login;
        this.date = date;
        this.status = status;
        this.productData = FXCollections.observableArrayList(products);
    }

    public Purchases(Integer id, String login, LocalDate date, List<String> products) {
        this(id, login, date, "Создан", products); // значение по умолчанию
    }

    public static ObservableList<Purchases> loadAll(String ordersFile, String productsFile) {
        ObservableList<Purchases> result = FXCollections.observableArrayList();

        Map<Integer, List<String>> productsByOrder = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(";");
                if (parts.length != 2) continue;

                int orderId = Integer.parseInt(parts[0]);
                String artikul = parts[1];

                productsByOrder
                        .computeIfAbsent(orderId, k -> new ArrayList<>())
                        .add(artikul);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла товаров заказов: " + e.getMessage());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(ordersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                int id = Integer.parseInt(parts[0]);
                String login = parts[1];
                LocalDate date = LocalDate.parse(parts[2], formatter);

                String status;
                if (parts.length >= 4) {
                    status = parts[3];
                } else {
                    status = "Создан";
                }

                List<String> products = productsByOrder.getOrDefault(id, new ArrayList<>());

                Purchases purchase = new Purchases(id, login, date, status, products);
                result.add(purchase);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла заказов: " + e.getMessage());
        }

        return result;
    }
    public static void saveOrders(String ordersFile, ObservableList<Purchases> purchases) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile))) {
            for (Purchases p : purchases) {
                String line = p.getId() + ";" +
                        p.getLogin() + ";" +
                        p.getDate().format(formatter) + ";" +
                        p.getStatus();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении заказов: " + e.getMessage());
        }
    }
    public static Purchases findById(ObservableList<Purchases> purchases, int id) {
        for (Purchases p : purchases) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public ObservableList<Product> getProducts() {
        ObservableList<Product> result = FXCollections.observableArrayList();

        if (allProducts == null || allProducts.isEmpty() || productData == null) {
            return result;
        }

        Map<String, Product> map = new HashMap<>();
        for (Product p : allProducts) {
            map.put(String.valueOf(p.getProductId()), p);
        }

        for (String artikul : productData) {
            Product prod = map.get(artikul);
            if (prod != null) {
                result.add(prod);
            }
        }

        return result;
    }

    public static void saveOrderProducts(String productsFile, ObservableList<Purchases> purchases) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {

            for (Purchases p : purchases) {
                for (String artikul : p.getProductData()) {
                    writer.write(p.getId() + ";" + artikul);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении order_products: " + e.getMessage());
        }
    }
}
