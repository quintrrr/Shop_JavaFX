package com.example.shop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productSumField;
    @FXML
    private TextField productCountField;

    private Stage dialogStage;
    private Product product;
    private boolean okClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setProduct(Product product){
        this.product = product;

        Integer id = product.getProductId();
        String name = product.getProductName();
        Double sum = product.getProductSum();
        Integer count = product.getProductCount();

        productIdField.setText(id != null ? id.toString() : "");
        productNameField.setText(name != null ? name : "");
        productSumField.setText(sum != null ? sum.toString() : "");
        productCountField.setText(count != null ? count.toString() : "");
    }

    public boolean isOkClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        String errorMessage = "";

        if (productIdField.getText() == null || productIdField.getText().isEmpty()){
            errorMessage += "Нет доступного артикула\n";
        }
        if (productNameField.getText() == null || productNameField.getText().isEmpty()){
            errorMessage += "Нет доступного наименования товара\n";
        }
        if (productSumField.getText() == null || productSumField.getText().isEmpty()){
            errorMessage += "Нет доступной суммы\n";
        }
        if (productCountField.getText() == null || productCountField.getText().isEmpty()){
            errorMessage += "Нет доступного наименования товара\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные поля");
            alert.setHeaderText("Внесите корректную информацию");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    protected void handleOk() {
        if (isInputValid()) {
            product.setProductId(Integer.parseInt(productIdField.getText()));
            product.setProductName(productNameField.getText());
            product.setProductSum(Double.parseDouble(productSumField.getText()));
            product.setProductCount(Integer.parseInt(productCountField.getText()));

            Product.addProduct(product);
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    protected void handleCancel() {
        dialogStage.close();
    }
}
