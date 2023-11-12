package com.example.filmboock;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HomeWindow homeWindow = new HomeWindow();
        homeWindow.openHomeWindow();
//        FilmPage filmPage = new FilmPage(1);
    }

    public static void main(String[] args) {
        launch();
    }
}