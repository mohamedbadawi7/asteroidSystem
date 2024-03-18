module com.example.as4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.as4 to javafx.fxml;
    exports com.example.as4;
}