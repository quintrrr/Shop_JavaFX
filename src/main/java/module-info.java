module com.example.shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.shop to javafx.fxml;
    exports com.example.shop;
}