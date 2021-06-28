package me.shaakashee.imgroup.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.shaakashee.imgroup.SaveData;
import me.shaakashee.imgroup.hashing.SimpleDifferenceHashing;
import me.shaakashee.imgroup.model.HashImage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SearchScreenCon {

    FileChooser fileChooser = new FileChooser();

    @FXML
    Button openSearch;

    @FXML
    Label pathLabel;

    @FXML
    VBox searchShow;

    @FXML
    Button more;

    @FXML
    Label status;

    ArrayList<SaveData.smallHashImg> resultList;
    int morePos = 0;
    String currentHash;

    private Stage stage;
    private ExecutorService exe;

    public void initialize(){
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.JPG", "*.JPEG"));
        openSearch.setOnAction(e -> searchImage());
        more.setOnAction(e -> showMoreResults());
    }

    public void showWindow(){
        stage.show();
        stage.toFront();
    }

    public void searchImage(){
        File file = fileChooser.showOpenDialog(stage);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> pathLabel.setText(file.getPath()));
                Image img = new Image("file:" + file.getAbsolutePath());
                String hash = SimpleDifferenceHashing.hashImage(img);
                currentHash = hash;
                showNearest(20, hash);
            }
        };
        exe.submit(run);
    }

    public void showNearest(int n, String hash){
        Platform.runLater(() -> status.setText("loading Hashes"));
        HashMap<String, SaveData.smallHashImg> imgs = SaveData.getSaved();
        Platform.runLater(() -> status.setText(imgs.values().size() + " Hashes loaded. Searching..."));
        resultList = imgs.values().parallelStream()
                .sorted(Comparator.comparingDouble(shi -> HashImage.getDistance(shi.hash, hash, 0.75, 1)))
                .collect(Collectors.toCollection(ArrayList::new));

        Platform.runLater(() -> status.setText(imgs.values().size() + " Hashes loaded. Searching completed."));
        ArrayList<SaveData.smallHashImg> nList = resultList.stream().limit(n).collect(Collectors.toCollection(ArrayList::new));
        morePos = n;

        Platform.runLater(() -> searchShow.getChildren().clear());
        for (SaveData.smallHashImg img: nList){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            Image loaded = new Image("file:" + img.path, 200, 200, true, true);
            ImageView imageView = new ImageView(loaded);
            imageView.setOnMouseClicked(e -> callOpen(img.path));
            Label label = new Label();
            label.setText("Distance: " + HashImage.getDistance(hash, img.hash, 0.75, 1));
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);
            Platform.runLater(()->searchShow.getChildren().add(hBox));
        }
    }

    public void showMoreResults(){
        if (resultList == null || currentHash == null){
            Logger.getGlobal().severe("no Results");
            return;
        }
        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (int i = morePos; i < morePos+20; i++){
                    SaveData.smallHashImg img = resultList.get(i);
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    Image loaded = new Image("file:" + img.path, 200, 200, true, true);
                    ImageView imageView = new ImageView(loaded);
                    imageView.setOnMouseClicked(e -> callOpen(img.path));
                    Label label = new Label();
                    label.setText("Distance: " + HashImage.getDistance(currentHash, img.hash, 0.75, 1));
                    hBox.getChildren().add(imageView);
                    hBox.getChildren().add(label);
                    Platform.runLater(() -> searchShow.getChildren().add(hBox));
                }

                morePos += 20;
            }
        };
        exe.submit(run);
    }

    public void openImage(String path){

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ExecutorService getExe() {
        return exe;
    }

    public void setExe(ExecutorService exe) {
        this.exe = exe;
    }

    public static void callOpen(String path){
        Logger.getGlobal().info("open Called");
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                File file = new File(path);
                desktop.open(file);
            } catch (IOException e) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Image not Found");
                        alert.showAndWait();
                    }
                });
            }
        }

    }
}
