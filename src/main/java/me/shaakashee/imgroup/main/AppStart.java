package me.shaakashee.imgroup.main;

import javafx.application.Application;
import javafx.stage.Stage;
import me.shaakashee.imgroup.controller.MainScreenCon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStart extends Application {

    MainScreenCon mainScreenCon;
    FXMLLoad mainScreenFxml;

    ExecutorService exe;

    @Override
    public void start(Stage stage) throws Exception {

        exe = Executors.newCachedThreadPool();
        stage.setOnCloseRequest(e -> exe.shutdown());

        mainScreenCon = new MainScreenCon();
        mainScreenCon.setStage(stage);
        mainScreenCon.setExe(exe);
        mainScreenFxml = new FXMLLoad("/me/shaakashee/imgroup/mainScreen.fxml", mainScreenCon);

        stage.setScene(mainScreenFxml.getScene());
        stage.show();

    }
}
