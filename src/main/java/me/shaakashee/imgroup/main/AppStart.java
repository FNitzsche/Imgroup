package me.shaakashee.imgroup.main;

import javafx.application.Application;
import javafx.stage.Stage;
import me.shaakashee.imgroup.controller.MainScreenCon;

public class AppStart extends Application {

    MainScreenCon mainScreenCon;
    FXMLLoad mainScreenFxml;

    @Override
    public void start(Stage stage) throws Exception {
        mainScreenCon = new MainScreenCon();
        mainScreenFxml = new FXMLLoad("/me/shaakashee/imgroup/mainScreen.fxml", mainScreenCon);

        stage.setScene(mainScreenFxml.getScene());
        stage.show();

    }
}
