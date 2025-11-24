package com.example.shop;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CatalogController implements Initializable, ChangeListener {
    private ObservableList<Product> productData;

    @FXML
    private TableView<Product> catalogTable;

    @FXML
    private TableColumn<Product, String> productId;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, String> productSum;

    @FXML
    private TableColumn<Product, String> productCount;

    @FXML
    private Label productIdLabel;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productSumLabel;

    @FXML
    private Label productCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productSum.setCellValueFactory(new PropertyValueFactory<>("productSum"));
        productCount.setCellValueFactory(new PropertyValueFactory<>("productCount"));

        if (productData == null) {
            productData = Product.loadProductsFromFile();
        }

        catalogTable.setItems(productData);

        showProductDetails(null);

        catalogTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue)
                        -> showProductDetails(newValue));

    }

    public void setProductData(ObservableList<Product> productData){
        this.productData = productData;
        catalogTable.setItems(productData);
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            productIdLabel.setText(product.getProductId().toString());
            productNameLabel.setText(product.getProductName());
            productSumLabel.setText(product.getProductSum().toString());
            productCountLabel.setText(product.getProductCount().toString());
        }
        else {
            productIdLabel.setText("");
            productNameLabel.setText("");
            productSumLabel.setText("");
            productCountLabel.setText("");
        }
    }

    @Override
    public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {

    }

    @FXML
    protected void handleDeleteProduct(){
        int selectedIndex = catalogTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            catalogTable.getItems().remove(selectedIndex);

            Product.saveProductsToFiles(catalogTable.getItems());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Не выделено");
            alert.setHeaderText("Не выбран товар");
            alert.setContentText("Выберите товар в таблице");

            alert.showAndWait();
        }
    }

    public boolean navigateToProductView(Product product){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ProductController.class.getResource("product-view.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ProductController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
           e.printStackTrace();
           return false;
        }
    }

    @FXML
    protected void handleNewProduct() throws IOException {
        Product tempProduct = new Product();

        boolean okClicked = this.navigateToProductView(tempProduct);
        if (okClicked) {
            productData.add(tempProduct);
            Product.addProduct(tempProduct);
        }
    }

    @FXML
    protected void handleEditProduct() {
        Product selectedProduct = catalogTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            boolean okClicked = navigateToProductView(selectedProduct);
            if (okClicked) {
                showProductDetails(selectedProduct);
                int selectedIndex = catalogTable.getSelectionModel().getSelectedIndex();
                productData.set(selectedIndex, selectedProduct);

                Product.saveProductsToFiles(catalogTable.getItems());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного продукта");
            alert.setContentText("Выберите продукт в таблице");

            alert.showAndWait();
        }
    }
}
