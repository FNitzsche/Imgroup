package me.shaakashee.imgroup.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.shaakashee.imgroup.ImageCollector;
import me.shaakashee.imgroup.KMeans;
import me.shaakashee.imgroup.model.Div;
import me.shaakashee.imgroup.model.HashImage;

import javax.lang.model.type.NullType;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

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

    @FXML
    Button cluster;
    @FXML
    Button save;

    @FXML
    Button back;
    @FXML
    ToggleButton sClusters;
    @FXML
    Label clLabel;

    private Div div = new Div();
    private Stage stage;
    private ExecutorService exe;
    HashImage selected;
    KMeans kMeans;

    public void initialize(){
        pathButton.setOnAction(e -> selectPath());
        read.setOnAction(e -> loadImages());
        hash.setOnAction(e -> getHashes());
        content.setOnMouseReleased(e -> showNearest(selected));
        color.setOnMouseReleased(e -> showNearest(selected));
        struc.setOnMouseReleased(e -> showNearest(selected));

        cluster.setOnAction(e -> calculateClusters());
        sClusters.setOnAction(e -> showClusters());
    }

    private void selectPath(){
        File path = directoryChooser.showDialog(stage);
        div.setPath(path);
        pathLabel.setText(path.getPath());
    }

    private void loadImages(){

        sClusters.setSelected(false);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                long s = System.currentTimeMillis();
                ImageCollector.setImages(div);
                Platform.runLater(() -> allLabel.setText("All Images: loading " + div.getImgs().size()));
                long se = System.currentTimeMillis();
                Platform.runLater(() -> calcAll());
                ImageCollector.loadPrevs(div);
                long e = System.currentTimeMillis();
                //Platform.runLater(() -> allLabel.setText("All Images: loaded " + div.getImgs().size()));
                System.out.println("Findtime: " + (se-s) + "ms, PrevTime: " + (e-se) + "ms");
            }
        };
        exe.submit(run);
    }

    private void calcAll(){

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.getAndSet(0);

        AtomicInteger atomicInteger2 = new AtomicInteger();
        atomicInteger.getAndSet(0);

        all.getChildren().clear();
        for (HashImage hashImage: div.getImgs()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            ImageView imageView = new ImageView();
            if (hashImage.getPrev() != null){
                imageView.setImage(hashImage.getPrev());
            }
            hBox.getChildren().add(imageView);
            Label label = new Label("not hashed");
            if (hashImage.getHash() != null){
                label.setText("hashed");
            }
            if (hashImage.getPrev() == null){
                label.setText("loading Image");
            }

            hashImage.propertyChangeSupport.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("prev")) {
                        atomicInteger.getAndIncrement();
                        Platform.runLater(() ->allLabel.setText("All Images: loading " + atomicInteger.get() + "/"+ div.getImgs().size() + " hashed: " + atomicInteger2));
                        Platform.runLater(() -> imageView.setImage(hashImage.getPrev()));
                        Platform.runLater(() -> label.setText("not hashed"));
                    }
                    if (propertyChangeEvent.getPropertyName().equals("hash")){
                        atomicInteger2.getAndIncrement();
                        Platform.runLater(() -> label.setText("hashed"));
                        Platform.runLater(() ->allLabel.setText("All Images: loading " + atomicInteger.get() + "/"+ div.getImgs().size() + " hashed: " + atomicInteger2));
                    }
                }
            });

            hBox.getChildren().add(label);
            all.getChildren().add(hBox);
            imageView.setOnMouseClicked(e -> {
                selected = hashImage;
                showNearest(hashImage);
            });
        }

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
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ImageCollector.loadHashes(div);
            }
        };
        exe.submit(run);
        //calcAll();
    }

    private void calculateClusters(){

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> clLabel.setText("clustering"));
                kMeans = new KMeans();
                kMeans.cluster(div, 5, content.getValue(), color.getValue());
                Platform.runLater(()-> clLabel.setText("clustered"));
            }
        };

        exe.submit(run);

    }

    private void showClusters(){
        if (!sClusters.isSelected()){
            calcAll();
            return;
        }
        if (kMeans == null || kMeans.lastClusters == null){
            System.out.println("No Clusters");
            return;
        }

        all.getChildren().clear();

        double c = content.getValue();
        double c2 = color.getValue();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                ArrayList<ImageView> reps = new ArrayList<>();
                int count = 0;
                for (int cl = 0; cl < kMeans.lastClusters.length; cl++){
                    HashImage nearest = null;
                    double minDist = 1000;
                    for (HashImage hashImage: div.getImgs()){
                        if (hashImage.getHash() == null){
                            continue;
                        }
                        double dist = HashImage.getDistance(kMeans.lastClusters[cl], hashImage.getHash(), c, c2);
                        if (dist != -1 && dist < minDist){
                            nearest = hashImage;
                            minDist = dist;
                        }
                    }
                    if (nearest != null){
                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        ImageView imageView = new ImageView(nearest.getPrev());
                        int finalCl = cl;
                        imageView.setOnMouseClicked((e) -> clusterSelected(finalCl, c, c2));
                        hBox.getChildren().add(imageView);
                        Label label = new Label("Cluster " + finalCl);
                        hBox.getChildren().add(label);
                        Platform.runLater(() ->  all.getChildren().add(hBox));
                    }
                }
            }
        };

        exe.submit(run);
    }

    private void clusterSelected(int i, double c, double c2){
        sorted.getChildren().clear();
        for (HashImage hashImage: KMeans.getForCluster(div, kMeans.lastClusters, i, c, c2)){
            ImageView imageView = new ImageView(hashImage.getPrev());
            Platform.runLater(() -> sorted.getChildren().add(imageView));
        }
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
}
