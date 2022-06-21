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
        /**
         * Load the FXML file and set the stage
         * @param stage TThe stage is the window that the application is displayed in
         * @param model The model is the data that is displayed in the window
         **/
        this.stage = stage;
        model = new SessionModel();

        loadScene("main-view.fxml");
        presenter.setup(this, model);
        setStage("plupper!", scene);

        this.stage.show();

    }

    private void setStage(String title, Scene scene){
        /**
         * Set the title and scene of the stage
         * @param title the title of the stage
         * @param scene the scene of the stage
         **/
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void loadScene(String resource){
        /**
         * Load the FXML file and create the scene
         * @param resource the name of the FXML file
         * */
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