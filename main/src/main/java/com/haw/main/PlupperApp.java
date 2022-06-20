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
            startScene = new Scene(loader.load());
            SessionPresenter controller = loader.<SessionPresenter>getController();
            controller.setApp(this);
            model = new SessionModel(controller);
            controller.setModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage = stage;
        this.stage.setTitle("plupper!");
        this.stage.setScene(startScene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void switchSceneHost(IService service) {

        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource("host-view.fxml"));
            sessionScene = new Scene(loader.load());

            SessionPresenter controller = loader.<SessionPresenter>getController();
            controller.setService(service);
            controller.setApp(this);
            controller.setModel(model);
            controller.bindModelToView();
            this.stage.setTitle("plupper! - Session");
            this.stage.setScene(sessionScene);
            this.stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchSceneGuest(IService service) {
        switchScene("guest-view.fxml", "plupper - Guest", service);
    }

    public static void main(String[] args) {
        launch();
    }
}