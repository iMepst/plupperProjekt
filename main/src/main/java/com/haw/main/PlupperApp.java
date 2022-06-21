package com.haw.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PlupperApp extends Application {

    private Stage stage;
    private Scene scene;
    private SessionPresenter presenter;
    private SessionModel model;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        model = new SessionModel();

        loadScene("main-view.fxml");
        presenter.setup(this, model);
        setStage("plupper!", scene);

        this.stage.show();

    }

    private void setStage(String title, Scene scene){
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void loadScene(String resource){
        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource(resource));
            scene = new Scene(loader.load());
            presenter = loader.<SessionPresenter>getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String resource, String title, IService service){
        /**
         * @param resource Represents the path to the FXML file
         * @param title The title to be shown in the new stage
         * @param service A reference to the networking service to hand to the new presenter
         */

        loadScene(resource);
        setStage(title, scene);
        presenter.setup(this, model, service);
    }

    public void switchSceneHost(IService service) {
        switchScene("host-view.fxml", "plupper - Host", service);
    }

    public void switchSceneGuest(IService service) {
        switchScene("guest-view.fxml", "plupper - Guest", service);
    }

    public static void main(String[] args) {
        launch();
    }
}