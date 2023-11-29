package com.example.filmboock;

import com.example.filmboock.base.Base;
import com.example.filmboock.base.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


//id image name year genre description actor collection
public class AddFilmWindow {
    Base base = new Base();
    private static final String PATH_IMAGE = "src/main/resources/Images/";
    Button imgButton = new Button(Lang.ADD_BUTTON_IMAGE);
    TextField nameText = new TextField();
    TextField yearText = new TextField();
    TextArea genreText = new TextArea();
    TextArea descriptionText = new TextArea();
    TextArea actorText = new TextArea();
    TextField collectionText = new TextField();
    Button addFilmButton = new Button(Lang.ADD_FORM_FILM);
    MenuButton menuAutocomplete = new MenuButton();

    Pane textAutoCompletePane = new Pane(collectionText,menuAutocomplete);
    Label infoLabel = new Label();
    HomeWindow homeWindow = null;

    public AddFilmWindow(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;
        VBox list = new VBox(imgButton, nameText,yearText,genreText,
                descriptionText,actorText,textAutoCompletePane,infoLabel,addFilmButton);
        list.setSpacing(10);
        list.setPadding(new Insets(10));
        list.setAlignment(Pos.CENTER);

        //Config
        infoLabel.setStyle("-fx-text-fill: red;");
        nameText.setPromptText(Lang.NAME_PROMPT_TEXT);
        yearText.setPromptText(Lang.YEAR_PROMPT_TEXT);
        genreText.setPromptText(Lang.GENRE_PROMPT_TEXT);
        descriptionText.setPromptText(Lang.DESCRIPTION_PROMPT_TEXT);
        descriptionText.setWrapText(true);
        actorText.setPromptText(Lang.ACTOR_PROMPT_TEXT);
        collectionText.setPromptText(Lang.COLLECTION_PROMPT_TEXT);
        collectionText.setMinWidth(480);
        collectionText.textProperty().addListener((observableValue, s, t1) -> addAutocompleteItems());
        menuAutocomplete.setVisible(false);

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
    private void addAutocompleteItems(){
        menuAutocomplete.getItems().clear();
        List<String> collections = base.getCollectionToValue(collectionText.getText());
        for(String collectionName : collections){
            MenuItem menuItemAutoCollection = new MenuItem(collectionName);
            menuItemAutoCollection.setOnAction(actionEvent -> collectionText.setText(menuItemAutoCollection.getText()));
            menuAutocomplete.getItems().add(menuItemAutoCollection);
        }
        if(!menuAutocomplete.getItems().isEmpty()){
            menuAutocomplete.show();
        }
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
        copyImaheInResource(copyOut, copyIn);
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
        imgButton.setText(Lang.ADD_BUTTON_IMAGE);
        nameText.setText("");
        yearText.setText("");
        genreText.setText("");
        descriptionText.setText("");
        actorText.setText("");
        collectionText.setText("");
        infoLabel.setText(Lang.FILM_ADD_SUCCESS);
        homeWindow.base = new Base();
        homeWindow.setContentFilm(base.getAllFilms());
    }
    File startDirectory;
    File copyOut = null;
    File copyIn = null;
    private void openFileDialogImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Lang.ADD_BUTTON_IMAGE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(startDirectory);
        copyOut = fileChooser.showOpenDialog(stage);

        if (copyOut != null) {
            String path = PATH_IMAGE + copyOut.getName();
            if (!isExistFile(path)) {
                copyIn = new File(path);
                startDirectory = new File(copyOut.getParent());
                imgButton.setText(copyOut.getName());
            } else {
                infoLabel.setText(Lang.ERROR_IMAGE_ADD);
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
        } catch (IOException ex) {
        }
    }
}
