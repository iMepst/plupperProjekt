package com.haw.main;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SessionPresenter {

    private PlupperApp app;
    private SessionModel model;
    private static IConnection network;

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextField enterNameText;

    @FXML
    private TextField messageText;

    @FXML
    protected void onStartServerClick(){
        app.switchScene();
        app.changeTitle("plupper - Host");
        SessionPresenter presenter = this;
        new Thread(() -> {
            network = new Server(presenter);
            network.start();
        }).start();
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onStartClientClick(){
        app.switchScene();
        app.changeTitle("plupper - Guest");
        SessionPresenter presenter = this;
        new Thread(() -> {
            network = new Client(presenter);
            network.start();
        }).start();
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onSendMessageClick() {
        String msg = messageText.getText();
        messageText.clear();
        network.sendMessage(msg);
    }

    @FXML
    protected void onCloseButtonClick(){
        network.stop();
    }

    public void receiveMessage(String msg){
        model.addMessage(msg);
    }

    public void bindModelToView(){
        messageList.setItems(model.getMessageList());
        messageList.getItems().addListener(
                (ListChangeListener<String>) change -> Platform.runLater( () -> messageList.scrollTo(messageList.getItems().size()))
        );
    }

    public void setApp(PlupperApp app){
        this.app = app;
    }
    public void setModel(SessionModel model) {
        this.model = model;
    }

}