package com.haw.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlupperApp extends Application {

    private Stage stage;
    private Scene startScene;
    private Scene sessionScene;
    private SessionModel model;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource("main-view.fxml"));
            startScene = new Scene(loader.load(), 1200, 700);

            SessionPresenter controller = loader.<SessionPresenter>getController();
            controller.setApp(this);
            model = new SessionModel(controller);
            controller.setModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage = stage;
        this.stage.setTitle("Welcome to plupper");
        this.stage.setScene(startScene);
        this.stage.show();
    }

    public void switchScene(){

        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource("host-view.fxml"));
            sessionScene = new Scene(loader.load(), 1200, 700);

            SessionPresenter controller = loader.<SessionPresenter>getController();
            controller.setApp(this);
            controller.setModel(model);
            controller.bindModelToView();
            this.stage.setTitle("plupper!");
            this.stage.setScene(sessionScene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeTitle(String title){
        this.stage.setTitle(title);
    }

    public static void main(String[] args) {
        launch();
    }
}