package com.example.shop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField textFieldLogin;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Hyperlink buttonLogin;

    @FXML
    protected void handleRegisterButton() {
        User user = new User(textFieldLogin.getText(), textFieldPassword.getText());

        if (user.IsRegistered()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Пользователь с таким именем уже существует");
            alert.setContentText("Введите другой логин");
            alert.showAndWait();
        }
        else {
            user.Add();

            NavigateToLogin();
        }
    }

    @FXML
    protected void handleLoginButton() {

        NavigateToLogin();
    }

    private void NavigateToLogin() {
        Stage stage = new Stage();
        stage.setTitle("Авторизация");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
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
