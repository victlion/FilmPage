module com.example.filmboock {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.filmboock to javafx.fxml;
    opens com.example.filmboock.base to com.google.gson;
    exports com.example.filmboock;
}