package com.haw.main;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SessionPresenter {

    private PlupperApp app;
    private SessionModel model;
    private IMessageService msgservice;
    private IService service;

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextField enterNameText;

    @FXML
    private TextField messageText;

    @FXML
    protected void onStartServerClick(){
        service = new Server(this);
        service.start();
        app.switchSceneHost(service);
        app.changeTitle("plupper - Host");

        System.out.println(service);
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onStartClientClick(){
        service = new Client(this);
        service.start();
        app.switchSceneGuest(service);
        app.changeTitle("plupper - Guest");
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onSendMessageClick() {
        String msg = messageText.getText();
        messageText.clear();
        service.sendMessage(msg);
    }

    @FXML
    protected void onMuteClick() {
        service.micSwitch();
    }

    @FXML
    protected void onCloseButtonClick(){
        service.stop();
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

    public void setService(IService service) {
        this.service = service;
    }
}