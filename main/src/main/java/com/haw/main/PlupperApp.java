package com.haw.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PlupperApp extends Application {

    private Stage stage;
    private SessionModel model;
    private SessionPresenter presenter;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource("main-view.fxml"));
            Scene startScene = new Scene(loader.load());
            presenter = loader.<SessionPresenter>getController();

            model = new SessionModel();
            presenter.setup(this, model);


            this.stage = stage;
            this.stage.setTitle("plupper!");
            this.stage.setScene(startScene);
            this.stage.setResizable(false);


            this.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void switchScene(String resource, String title, IService service){
        /**
         * @param resource Represents the path to the FXML file
         * @param title The title to be shown in the new stage
         * @param service A reference to the networking service to hand to the new presenter
         */
        try {
            FXMLLoader loader = new FXMLLoader(PlupperApp.class.getResource(resource));
            Scene sessionScene = new Scene(loader.load());
            presenter = loader.<SessionPresenter>getController();

            presenter.setup(this, model, service);

            this.stage.setTitle(title);
            this.stage.setScene(sessionScene);
            this.stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
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