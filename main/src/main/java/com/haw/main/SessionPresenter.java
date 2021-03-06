package com.haw.main;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SessionPresenter implements IPresenter {

    private PlupperApp app;
    private SessionModel model;
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
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onStartClientClick(){
        service = new Client(this);
        service.start();
        app.switchSceneGuest(service);
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onSendMessageClick() {
        if(! messageText.getText().equals("")){
            String msg = model.getUser().getName() + ": " + messageText.getText();
            messageText.clear();
            service.sendMessage(msg);
        }
    }

    @FXML
    protected void onMuteClick() {
        service.micSwitch();
    }

    @FXML
    protected void onCloseButtonClick(){
        service.stop();
        Platform.exit();
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

    public void setup(PlupperApp app, SessionModel model){
        this.app = app;
        this.model = model;
    }

    public void setup(PlupperApp app, SessionModel model, IService service){
        setup(app, model);
        this.service = service;
        bindModelToView();
    }


}