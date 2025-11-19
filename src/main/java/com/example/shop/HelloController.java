package com.example.shop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField textFieldLogin;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Label labelCompareResult;

    @FXML
    private Hyperlink buttonRegister;

    @FXML
    private Button buttonLogin;

    @FXML
    protected void handleLoginButton() {
        User user = new User(textFieldLogin.getText(), textFieldPassword.getText());

        if (user.Compare()) {
            NavigateToCatalog();
        }
        else {
            labelCompareResult.setText("Неверный логин или пароль");
        }
    }

    @FXML
    protected void handleCancelButton() {
        textFieldLogin.setText("");
        textFieldPassword.setText("");

        labelCompareResult.setText("");
    }

    @FXML
    protected void handleRegisterButton() {

        NavigateToRegister();
    }


    private void NavigateToRegister() {
        Stage stage = new Stage();
        stage.setTitle("Регистрация");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Stage stage1 = (Stage) buttonRegister.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void NavigateToCatalog() {
        Stage stage = new Stage();
        stage.setTitle("Каталог");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("catalog-view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Stage stage1 = (Stage) buttonLogin.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
