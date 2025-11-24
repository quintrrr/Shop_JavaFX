package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Tab catalogTab;
    @FXML
    private Tab productTab;
    @FXML
    private TabPane tabPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Purchases> purchaseTable;
    @FXML
    private TableColumn<Purchases, String> purchaseId;
    @FXML
    private TableColumn<Purchases, String> purchaseName;

    @FXML
    private Label purchaseIdLabel;
    @FXML
    private Label totalSumLabel;
    @FXML
    private ComboBox<String> purchaseStatus;
    @FXML
    private TableView<Product> orderProductsTable;
    @FXML
    private TableColumn<Product, String> orderProductName;
    @FXML
    private TableColumn<Product, Integer> orderProductCount;
    @FXML
    private TableColumn<Product, Double> orderProductPrice;


    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private ObservableList<String> statusList = FXCollections.observableArrayList();
    private ObservableList<Purchases> purchaseData = FXCollections.observableArrayList();;

    private CatalogController catalogController;

    public void initialize(){
        initCatalogTab();
        initProductTab();
    }

    private void initCatalogTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog-view.fxml"));
            Parent root = loader.load();
            catalogController = loader.getController();
            productData.setAll(Product.loadProductsFromFile());
            Purchases.setAllProducts(productData);
            catalogController.setProductData(productData);
            catalogTab.setContent(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка загрузки интерфейса");
            alert.setHeaderText("Не удалось загрузить окно каталога");
            alert.setContentText("Проверьте файл catalog-view.fxml");

            alert.showAndWait();

            e.printStackTrace();
        }
    }

    private void initProductTab() {
        purchaseId.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchaseName.setCellValueFactory(new PropertyValueFactory<>("login"));

        purchaseData.setAll(Purchases.loadAll("orders.txt", "order_products.txt"));
        purchaseTable.setItems(purchaseData);

        orderProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orderProductCount.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        orderProductPrice.setCellValueFactory(new PropertyValueFactory<>("productSum"));

        statusList.addAll("Создан", "Оплачен", "Доставлен");
        purchaseStatus.setItems(statusList);
        purchaseStatus.setDisable(true);

        purchaseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> showPurchaseDetails(newValue)
        );

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldText, newText) -> searchPurchase(newText));
        }
    }

    private void showPurchaseDetails(Purchases purchase) {
        if (purchase == null) {
            purchaseIdLabel.setText("");
            totalSumLabel.setText("");
            purchaseStatus.setValue(null);
            orderProductsTable.getItems().clear();   // вот так
            return;
        }

        purchaseIdLabel.setText(String.valueOf(purchase.getId()));

        purchaseStatus.setValue(purchase.getStatus());

        ObservableList<Product> productsInOrder = purchase.getProducts();

        orderProductsTable.setItems(productsInOrder);

        double sum = 0.0;
        for (Product p : productsInOrder) {
            sum += p.getProductSum();
        }
        totalSumLabel.setText(String.valueOf(sum));
    }

    @FXML
    protected void selectCatalogTab(ActionEvent event) {
        tabPane.getSelectionModel().select(catalogTab);
    }

    @FXML
    protected void selectProductTab(ActionEvent event) {
        tabPane.getSelectionModel().select(productTab);
    }

    @FXML
    protected void selectOpenCatalogView(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Каталог товаров");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog-view.fxml"));
            Parent root = loader.load();

            CatalogController controller = loader.getController();
            controller.setProductData(productData);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка загрузки интерфейса");
            alert.setHeaderText("Не удалось загрузить окно каталога");
            alert.setContentText("Проверьте файл catalog-view.fxml");

            alert.showAndWait();

            e.printStackTrace();
        }
    }

    @FXML
    protected void selectInfo(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("О приложении");
        alert.setContentText("Программа учебная. Версия 1.0");

        alert.showAndWait();

    }

    @FXML
    protected void onEditStatusClicked(ActionEvent event) {
        purchaseStatus.setDisable(false);
    }

    @FXML
    protected void onSaveOrderClicked(ActionEvent event) {
        Purchases selected = purchaseTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String newStatus = purchaseStatus.getValue();
        selected.setStatus(newStatus);

        purchaseStatus.setDisable(true);

        Purchases.saveOrders("orders.txt", purchaseData);
        Purchases.saveOrderProducts("order_products.txt", purchaseData);
    }

    private void searchPurchase(String text) {
        if (text == null || text.isBlank()) {
            purchaseTable.getSelectionModel().clearSelection();
            return;
        }

        try {
            int id = Integer.parseInt(text);

            for (int i = 0; i < purchaseData.size(); i++) {
                if (purchaseData.get(i).getId() == id) {
                    purchaseTable.getSelectionModel().select(i);
                    purchaseTable.scrollTo(i);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            purchaseTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    protected void onCancelOrderClicked(ActionEvent event) {
        purchaseStatus.setDisable(true);

        Purchases selected = purchaseTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            purchaseStatus.setValue(selected.getStatus());
        } else {
            purchaseStatus.setValue(null);
        }
    }

    @FXML
    private void handleShowOrdersStatistics() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("diagram-view.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Статистика продаж");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);
        dialogStage.setScene(new Scene(root));

        DiagramController controller = loader.getController();

        controller.fillProductNames(productData);

        controller.setProductData(purchaseData);

        dialogStage.show();
    }
}
