package com.example.filmboock;

import com.example.filmboock.base.Base;
import com.example.filmboock.base.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Arrays;


//id image name year genre description actor collection
public class AddFilmWindow {
    Base base = new Base();
    private static final String PATH_IMAGE = "DATA/Images/";
    Button imgButton = new Button(Lang.ADD_BUTTON_IMAGE);
    TextField nameText = new TextField();
    TextField yearText = new TextField();
    TextArea genreText = new TextArea();
    TextField descriptionText = new TextField();
    TextArea actorText = new TextArea();
    TextField collectionText = new TextField();
    Button addFilmButton = new Button(Lang.ADD_FORM_FILM);

    Label infoLabel = new Label();

    public AddFilmWindow() {
        VBox list = new VBox(imgButton, nameText,yearText,genreText,
                descriptionText,actorText,collectionText,infoLabel,addFilmButton);
        list.setSpacing(10);
        list.setPadding(new Insets(10));
        list.setAlignment(Pos.CENTER);

        //Config
        infoLabel.setStyle("-fx-text-fill: red;");
        nameText.setPromptText(Lang.NAME_PROMPT_TEXT);
        yearText.setPromptText(Lang.YEAR_PROMPT_TEXT);
        genreText.setPromptText(Lang.GENRE_PROMPT_TEXT);
        descriptionText.setPromptText(Lang.DESCRIPTION_PROMPT_TEXT);
        actorText.setPromptText(Lang.ACTOR_PROMPT_TEXT);
        collectionText.setPromptText(Lang.COLLECTION_PROMPT_TEXT);

        addFilmButton.setOnAction(event -> addFilmButtonEvent());
        imgButton.setOnAction(event -> openFileDialogImage());
        genreText.setWrapText(true);
        genreText.setMaxHeight(60);
        actorText.setWrapText(true);
        actorText.setMaxHeight(60);
        //

        Scene scene = new Scene(list, 500, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle(Lang.ADD_FORM_FILM);
        stage.setScene(scene);
        stage.show();
    }
    private void addFilmButtonEvent(){
        if(Objects.equals(imgButton.getText(), Lang.ADD_BUTTON_IMAGE)){
            infoLabel.setText(Lang.ERROR_NO_IMAGE_ADD);
        }else{
            if(nameText.getText().trim().isEmpty() || yearText.getText().trim().isEmpty()){
                infoLabel.setText(Lang.ERROR_NO_NAME_OR_YEAR);
            }
            else{
                writeData();
            }
        }
    }
    private void writeData(){
        List<String> genreList = List.of(genreText.getText().split(","));
        genreList = genreList.stream().map(String::trim)
                .filter(e -> ! e.isEmpty())
                .collect(Collectors.toList());
        List<String> actorList = List.of(actorText.getText().split(","));
        actorList = actorList.stream().map(String::trim)
                .filter(e -> ! e.isEmpty())
                .collect(Collectors.toList());
        base.writer(PATH_IMAGE+imgButton.getText(),nameText.getText().trim(), yearText.getText().trim(),genreList,
                descriptionText.getText().trim(),actorList,collectionText.getText().trim());
    }
    File startDirectory;
    private void openFileDialogImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Lang.ADD_BUTTON_IMAGE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(startDirectory);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String path = PATH_IMAGE + file.getName();
            if (!isExistFile(path)) {
                File copied = new File(path);
                startDirectory = new File(file.getParent());
                imgButton.setText(file.getName());
                copyFileUsingChannel(file, copied);
            } else {
                infoLabel.setText(Lang.ERROR_IMAGE_ADD);
            }
        }
    }
    private boolean isExistFile(String path) {
        return new File(path).exists();
    }
    private void copyFileUsingChannel(File source, File dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
        } catch (IOException ex) {
        }
    }
}
