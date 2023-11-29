package com.example.filmboock;

import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {
    @Override
    public void start(Stage stage) {
        HomeWindow homeWindow = new HomeWindow();
        homeWindow.openHomeWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}