package com.haw.main;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SessionPresenter {

    private PlupperApp app;
    private SessionModel model;
    private static Server server;
    private static Client client;

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
        startServer(this);


        //model.setUser(new User(enterNameText.getText()));
    }

    public void bindModelToView(){
        messageList.setItems(model.getMessageList());
    }

    @FXML
    protected void onStartClientClick(){
        app.switchScene();
        app.changeTitle("plupper - Guest");
        startClient(this);
        model.setUser(new User(enterNameText.getText()));
    }

    @FXML
    protected void onSendMessageClick() {
        String msg = messageText.getText() + "\n";
        client.sendMessage(msg);
        messageText.clear();
    }


    public void setApp(PlupperApp app){
        this.app = app;
    }
    public void setModel(SessionModel model) {
        this.model = model;
    }


    public void startServer(SessionPresenter controller){
        new Thread(){
            @Override
            public void run(){
                server = new Server(controller);
                server.start();
            }

        }.start();

    }

    public void startClient(SessionPresenter controller){
        new Thread(){
            @Override
            public void run(){
                client = new Client(controller);
                client.start();
            }
        }.start();
    }

    public void receiveMessage(String msg){
        model.addMessage(msg);
    }




}