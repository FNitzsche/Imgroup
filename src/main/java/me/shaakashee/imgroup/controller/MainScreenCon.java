package me.shaakashee.imgroup.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.shaakashee.imgroup.ImageCollector;
import me.shaakashee.imgroup.model.Div;
import me.shaakashee.imgroup.model.HashImage;
import java.io.File;
import java.util.ArrayList;

public class MainScreenCon {

    DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    Button pathButton;
    @FXML
    Label pathLabel;
    @FXML
    Button read;
    @FXML
    Button hash;
    @FXML
    VBox all;
    @FXML
    VBox sorted;
    @FXML
    Label allLabel;

    @FXML
    Slider content;
    @FXML
    Slider color;
    @FXML
    Slider struc;

    private Div div = new Div();
    private Stage stage;
    HashImage selected;

    public void initialize(){
        pathButton.setOnAction(e -> selectPath());
        read.setOnAction(e -> loadImages());
        hash.setOnAction(e -> getHashes());
        content.setOnMouseReleased(e -> showNearest(selected));
        color.setOnMouseReleased(e -> showNearest(selected));
        struc.setOnMouseReleased(e -> showNearest(selected));
    }

    private void selectPath(){
        File path = directoryChooser.showDialog(stage);
        div.setPath(path);
        pathLabel.setText(path.getPath());
    }

    private void loadImages(){
        long s = System.currentTimeMillis();
        ImageCollector.setImages(div);
        long se = System.currentTimeMillis();
        ImageCollector.loadPrevs(div);
        long e = System.currentTimeMillis();
        System.out.println("Findtime: " + (se-s) + "ms, PrevTime: " + (e-se) + "ms");
        calcAll();
    }

    private void calcAll(){
        all.getChildren().clear();
        for (HashImage hashImage: div.getImgs()){
            if (hashImage.getPrev() != null){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                ImageView imageView = new ImageView(hashImage.getPrev());
                hBox.getChildren().add(imageView);
                if (hashImage.getHash() != null){
                    TextArea textArea = new TextArea();
                    textArea.setWrapText(true);
                    textArea.setPrefHeight(100);
                    textArea.setPrefWidth(150);
                    textArea.setText(hashImage.getHash());
                    textArea.setEditable(false);
                    hBox.getChildren().add(textArea);
                }
                all.getChildren().add(hBox);
                imageView.setOnMouseClicked(e -> {
                    selected = hashImage;
                    showNearest(hashImage);
                });
            }
        }
        allLabel.setText("All Images: " + div.getImgs().size());
    }

    private void showNearest(HashImage hashImage){
        if (hashImage == null){
            return;
        }
        ArrayList<HashImage> sImgs = ImageCollector.getDistTo(hashImage, div, content.getValue(), color.getValue(), struc.getValue());
        sorted.getChildren().clear();
        for (HashImage hashImage1: sImgs){
            if (hashImage1.getPrev() != null){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                ImageView imageView = new ImageView(hashImage1.getPrev());
                hBox.getChildren().add(imageView);
                Label label = new Label();
                label.setText("Dist: " + hashImage1.getCurrentDistance());
                hBox.getChildren().add(label);
                sorted.getChildren().add(hBox);
            }
        }
    }

    private void getHashes(){
        ImageCollector.loadHashes(div);
        calcAll();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
