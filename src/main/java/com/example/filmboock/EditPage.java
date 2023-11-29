package com.example.filmboock;

import com.example.filmboock.base.Base;
import com.example.filmboock.base.Film;
import com.example.filmboock.base.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.stream.Collectors;

public class EditPage {
    private static final String PATH_IMAGE = "src/main/resources/Images/";
    private static final String PATH_IMAGE_SAVE = "src/main/resources/System/save.png";
    TextField nameText = new TextField();
    TextField yearText = new TextField();
    TextArea genreText = new TextArea();
    TextArea descriptionText = new TextArea();
    TextArea actorText = new TextArea();
    TextField collectionText = new TextField();
    HBox content = new HBox();
    Base base = new Base();
    FilmPage filmPage = null;
    Button imageButton = null;
    Film thisFilm = null;
    Stage stage = null;
    HomeWindow homeWindow = null;
    public EditPage(FilmPage filmPage,double w,double h,int id,HomeWindow homeWindow){
        this.homeWindow = homeWindow;
        this.filmPage = filmPage;
        filmPage.stage.close();
        Scene scene = new Scene(content,w,h);
        //
        setContent(base.getFilmToId(id),"");
        //
        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    private void setContent(Film film,String pathImage){
        content.getChildren().clear();
        thisFilm = film;
        if(pathImage.isEmpty()) {
            imageButton = getImageView(film.getImage());
        }else{
            imageButton = getImageView(pathImage);
        }
        imageButton.setOnAction(actionEvent -> openFileDialogImage());
        VBox listTextFilm = getListTextFilm(film);
        content.getChildren().addAll(imageButton,listTextFilm);
        //config
        content.setPadding(new Insets(10));
        content.setSpacing(10);
    }
    private VBox getListTextFilm(Film film){
        VBox result = new VBox();
        result.setAlignment(Pos.CENTER);
        result.setSpacing(10);
        //
        HBox nameText_buttonSave = getNameAndButton();
        nameText_buttonSave.setSpacing(20);
        nameText.setText(film.getName());
        nameText.setMinWidth(430);
        yearText.setText(film.getYear());
        genreText.setText(String.join(",",film.getGenre()));
        genreText.setWrapText(true);
        descriptionText.setText(film.getDescription());
        descriptionText.setWrapText(true);
        actorText.setText(String.join(",",film.getActor()));
        actorText.setWrapText(true);
        collectionText.setText(film.getCollection());

        result.getChildren().addAll(nameText_buttonSave,yearText,genreText,descriptionText,actorText,collectionText);
        return result;
    }
    private HBox getNameAndButton(){
        ImageView imageView = null;
        try {
            FileInputStream fis = new FileInputStream(PATH_IMAGE_SAVE);
            Image image = new Image(fis);
            imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
        }catch (IOException ex){}
        Button buttonSave = new Button();
        buttonSave.setOnAction(actionEvent -> reWriteFilm());
        buttonSave.setGraphic(imageView);
        return new HBox(nameText,buttonSave);
    }
    private void reWriteFilm(){
        List<String> genreList = List.of(genreText.getText().split(","));
        genreList = genreList.stream().map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
        List<String> actorList = List.of(actorText.getText().split(","));
        actorList = actorList.stream().map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());

        base.reWriteFilm(thisFilm.getId(), getNewImage(),nameText.getText().trim(), yearText.getText().trim(),genreList,
                descriptionText.getText().trim(),actorList,collectionText.getText().trim());

        stage.close();
        new FilmPage(thisFilm.getId(),homeWindow);

        homeWindow.base = new Base();
        homeWindow.setContentFilm(base.getAllFilms());
    }
    private String getNewImage(){
        String result = "";
        if(copyOut != null) {
            copyImaheInResource(copyOut,copyIn);
            result = copyIn.toString();
        }else {
            result = thisFilm.getImage();
        }
        return result;
    }
    private Button getImageView(String path){
        ImageView imageView = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            Image image = new Image(fis);
            imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(400);
        }catch (IOException ex){}
        Button buttonResult = new Button();
        buttonResult.setGraphic(imageView);
        return buttonResult;
    }
    File copyOut = null;
    File copyIn = null;
    private void openFileDialogImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Lang.ADD_BUTTON_IMAGE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        copyOut = fileChooser.showOpenDialog(stage);

        if (copyOut != null) {
            String pathImage = PATH_IMAGE + copyOut.getName();
            if (!isExistFile(pathImage)) {
                copyIn = new File(pathImage);
                setContent(thisFilm,copyOut.getPath());
            } else {
//                infoLabel.setText(Lang.ERROR_IMAGE_ADD);
            }
        }
    }
    private boolean isExistFile(String path) {
        return new File(path).exists();
    }
    private void copyImaheInResource(File source, File dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
        } catch (IOException ex){}
    }
}
