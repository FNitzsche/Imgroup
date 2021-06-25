package me.shaakashee.imgroup.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.shaakashee.imgroup.model.Div;

import java.io.File;

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

    private Div div = new Div();
    private Stage stage;

    public void initialize(){
        pathButton.setOnAction(e -> selectPath());
    }

    private void selectPath(){
        File path = directoryChooser.showDialog(stage);
        div.setPath(path);
        pathLabel.setText(path.getPath());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
