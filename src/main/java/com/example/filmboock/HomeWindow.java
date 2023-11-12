package com.example.filmboock;

import com.example.filmboock.base.Base;
import com.example.filmboock.base.Film;
import com.example.filmboock.base.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Map;

public class HomeWindow {
    Base base = new Base();
    VBox content = new VBox();
    FlowPane contentFilm = new FlowPane();
    ScrollPane scrollcontentFilm = new ScrollPane(contentFilm);

    public void openHomeWindow() {
        Scene scene = new Scene(content);
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();
        //
        setMenu();
        scrollcontentFilm.setFitToWidth(true);
        //
        content.getChildren().add(scrollcontentFilm);
        List<Film> listFilm = base.getAllFilms();
        setContentFilm(listFilm);
    }

    public void setContentFilm(List<Film> films) {
        contentFilm.getChildren().clear();
        for (Film film : films) {
            File file = new File(film.getImage());
            try {
                FileInputStream inputFileImage = new FileInputStream(film.getImage());
                Image image = new Image(inputFileImage);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(130);

                Label nameFilmLabel = new Label(film.getName() + "(" + film.getYear() + ")");
                nameFilmLabel.setMaxWidth(130);
                nameFilmLabel.setWrapText(true);
                nameFilmLabel.setTextAlignment(TextAlignment.CENTER);

                Button newButtonFilm = new Button();
                newButtonFilm.setOnAction(actionEvent -> new FilmPage(film.getId(), this));
                newButtonFilm.setGraphic(imageView);
                VBox vBoxFilm = new VBox(newButtonFilm, nameFilmLabel);
                vBoxFilm.setPadding(new Insets(10));
                vBoxFilm.setAlignment(Pos.TOP_CENTER);

                contentFilm.getChildren().add(vBoxFilm);
            } catch (IOException ex) {
            }
        }
    }

    private void setMenu() {
        Menu menuGenre = new Menu(Lang.MENU_GENRE);
        Menu menuYear = new Menu(Lang.MENU_YEAR);
        Menu menuAddFilm = new Menu(Lang.MENU_WINDOW);

        //addMenu
        Map<String, String> genreList = base.getGenreList();
        Map<String, String> yearList = base.getYearList();
        for (String genre : genreList.keySet()) {
            MenuItem newItemGenre = new MenuItem(genre);
            newItemGenre.setOnAction(actionEvent -> setContentFilm(base.getFilmToGenre(newItemGenre.getText())));
            menuGenre.getItems().add(newItemGenre);
        }
        for (String year : yearList.keySet()) {
            MenuItem newItemYear = new MenuItem(year);
            newItemYear.setOnAction(actionEvent -> setContentFilm(base.getFilmToYear(newItemYear.getText())));
            menuYear.getItems().add(newItemYear);
        }
        //Config
        MenuItem openWindowAddFilmItem = new MenuItem(Lang.MENU_ADD_FILM);
        openWindowAddFilmItem.setOnAction(actionEvent -> openWindowAddFilm());
        menuAddFilm.getItems().add(openWindowAddFilmItem);
        //
        MenuBar menuBar = new MenuBar(menuGenre, menuYear, menuAddFilm);
        //
        Button reloadHomWindow = new Button(Lang.RELOAD);
        reloadHomWindow.setOnAction(actionEvent -> {
            setContentFilm(base.getAllFilms());
            base = new Base();
        });
        reloadHomWindow.setDefaultButton(true);
        //
        TextField searchText = new TextField();
        searchText.textProperty().addListener((observableValue, s, t1) ->
                setContentFilm(base.getFilmToName(searchText.getText())));
        //
        HBox dropMenu = new HBox(menuBar, reloadHomWindow, searchText);
        dropMenu.setSpacing(10);
        content.getChildren().add(dropMenu);
    }

    private void openWindowAddFilm() {
        AddFilmWindow addFilmWindow = new AddFilmWindow();
    }
}
