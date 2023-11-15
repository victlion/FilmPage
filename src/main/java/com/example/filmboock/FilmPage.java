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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Flow;

public class FilmPage {
    private static final String PATH_IMAGE_EDIT = "DATA/Images/System/edit.png";
    private static final String PATH_IMAGE_DEL = "DATA/Images/System/delete.png";
    HBox content = new HBox();
    HomeWindow homeWindow;
    Base base = new Base();
    Stage stage = new Stage();
    double w = 850;
    double h = 450;
    public FilmPage(int id,HomeWindow homeWindow) {
        this.homeWindow = homeWindow;
        FilmPageStart(id);
    }
    private void FilmPageStart(int id){
        Scene scene = new Scene(content,w,h);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        setContent(base.getFilmToId(id));
    }
    private void setContent(Film film){
        content.getChildren().clear();
        //config
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        //
//id image name year genre description actor collection
        HBox nameFilm = getNameEditDeleteFilm(film.getName(), film.getId());
        Label yearFilm = new Label(film.getYear());
        FlowPane genreButtonPanel = new FlowPane(getGenreButtons(film));
        Accordion accordionDescription = getAccordionDescription(film);
        FlowPane actorButtonPanel = new FlowPane(getActorButtons(film));
        FlowPane collectionButtonPanel = new FlowPane(getCollection(film));

        VBox listInfoFilm = new VBox(nameFilm,yearFilm,genreButtonPanel,
                accordionDescription,actorButtonPanel,collectionButtonPanel);
        listInfoFilm.setSpacing(20);
        listInfoFilm.setPadding(new Insets(5));
        ScrollPane scrollPaneContent = new ScrollPane(listInfoFilm);
        scrollPaneContent.setFitToWidth(true);

        content.getChildren().addAll(getPosterImage(film),scrollPaneContent);

    }
    private HBox getNameEditDeleteFilm(String name,int id){
        ImageView editImageView = null;
        ImageView delImageView = null;
        try {
            FileInputStream imageFileEdit = new FileInputStream(PATH_IMAGE_EDIT);
            FileInputStream imageFileDel = new FileInputStream(PATH_IMAGE_DEL);
            Image imageEdit = new Image(imageFileEdit);
            Image imageDel = new Image(imageFileDel);
            editImageView = new ImageView(imageEdit);
            editImageView.setFitHeight(20);
            editImageView.setFitWidth(20);
            delImageView = new ImageView(imageDel);
            delImageView.setFitWidth(20);
            delImageView.setFitHeight(20);
        }catch (IOException ex){}

        Button buttonEdit = new Button();
        buttonEdit.setGraphic(editImageView);
        buttonEdit.setOnAction(actionEvent -> new EditPage(this,w,h,id,homeWindow));
        Button buttonDel = new Button();
        buttonDel.setOnAction(actionEvent -> deletePageFilm(id,name));
        buttonDel.setGraphic(delImageView);
        HBox buttons = new HBox(buttonEdit,buttonDel);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(0,0,0,10));

        HBox result = new HBox(new Label(name),buttons);
        return result;
    }
    private Button[] getGenreButtons(Film film){
        Button[] genreButtons = new Button[film.getGenre().size()];
        for(int i = 0;i<film.getGenre().size();i++){
            Button newGenreButton = new Button(film.getGenre().get(i));
            newGenreButton.setOnAction(actionEvent -> homeWindow.
                    setContentFilm(base.getFilmToGenre(newGenreButton.getText())));
            genreButtons[i] = newGenreButton;
        }
        return genreButtons;
    }
    private Accordion getAccordionDescription(Film film){
        Accordion accordionDescription = new Accordion();
        Label descriptionLabel = new Label(film.getDescription());
        descriptionLabel.setWrapText(true);
        TitledPane descriptionPane = new TitledPane(Lang.DESCRIPTION, descriptionLabel);
        accordionDescription.getPanes().add(descriptionPane);
        return accordionDescription;
    }
    private Button[] getActorButtons(Film film){
        Button[] actorButtons = new Button[film.getActor().size()];
        for(int i = 0;i<film.getActor().size();i++){
            Button newActorButton = new Button(film.getActor().get(i));
            newActorButton.setOnAction(actionEvent -> homeWindow.
                    setContentFilm(base.getFilmToActor(newActorButton.getText())));
            actorButtons[i] = newActorButton;
        }
        return actorButtons;
    }
    private ImageView getPosterImage(Film film){
        ImageView posterImage = null;
        try {
            FileInputStream fos = new FileInputStream(film.getImage());
            Image image = new Image(fos);
            posterImage = new ImageView(image);
            posterImage.setFitWidth(300);
            posterImage.setFitHeight(400);
        }catch (IOException ex){}
        return posterImage;
    }
    private Button[] getCollection(Film film){
        List<Film> filmListCollection = base.getFilmToCollection(film.getCollection());
        Button[] result = new Button[filmListCollection.size()];
        for(int i=0 ; i<filmListCollection.size();i++){
            ImageView posterImage = null;
            try {
                FileInputStream fos = new FileInputStream(filmListCollection.get(i).getImage());
                Image image = new Image(fos);
                posterImage = new ImageView(image);
                posterImage.setFitWidth(100);
                posterImage.setFitHeight(150);
            }catch (IOException ex){}
            Button posterCollectionButton = new Button();
            int finalI = i;
            Tooltip.install(posterCollectionButton, new Tooltip(filmListCollection.get(finalI).getName()));
            posterCollectionButton.setOnAction(actionEvent -> new FilmPage(filmListCollection.get(finalI).getId(),homeWindow));
            posterCollectionButton.setGraphic(posterImage);
            result[i] = posterCollectionButton;
        }
        return result;
    }
    private void deletePageFilm(int id,String name){
        Button buttonDelete = new Button(Lang.CONFIRM_DELETE + name);
        buttonDelete.setWrapText(true);
        buttonDelete.setTextAlignment(TextAlignment.CENTER);
        Scene sceneDelete = new Scene(buttonDelete,250,100);
        Stage stageDeletePage = new Stage();
        stageDeletePage.setScene(sceneDelete);
        stageDeletePage.setResizable(false);
        buttonDelete.setOnAction(actionEvent -> {
            base.removeFilm(id);
            homeWindow.setContentFilm(base.getAllFilms());
            homeWindow.base = new Base();
            stage.close();
            stageDeletePage.close();
        });
        stageDeletePage.show();
    }
}
