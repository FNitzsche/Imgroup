package me.shaakashee.imgroup.main;

import javafx.application.Application;
import javafx.stage.Stage;
import me.shaakashee.imgroup.controller.MainScreenCon;
import me.shaakashee.imgroup.controller.SearchScreenCon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStart extends Application {

    MainScreenCon mainScreenCon;
    FXMLLoad mainScreenFxml;

    SearchScreenCon searchScreenCon;
    FXMLLoad searchScreenFxml;

    ExecutorService exe;

    @Override
    public void start(Stage stage) throws Exception {

        exe = Executors.newCachedThreadPool();
        stage.setOnCloseRequest(e -> exe.shutdown());

        mainScreenCon = new MainScreenCon();
        mainScreenCon.setStage(stage);
        mainScreenCon.setExe(exe);

        searchScreenCon =  new SearchScreenCon();
        searchScreenCon.setStage(new Stage());
        searchScreenCon.setExe(exe);

        mainScreenCon.setSearchScreenCon(searchScreenCon);

        mainScreenFxml = new FXMLLoad("/me/shaakashee/imgroup/mainScreen.fxml", mainScreenCon);

        searchScreenFxml = new FXMLLoad("/me/shaakashee/imgroup/searchScreen.fxml", searchScreenCon);

        searchScreenCon.getStage().setScene(searchScreenFxml.getScene());



        stage.setScene(mainScreenFxml.getScene());
        stage.show();

    }
}
