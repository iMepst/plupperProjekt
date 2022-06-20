package com.haw.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SessionModel {

    private ObservableList<String> messages;
    private User user;

    public SessionModel(){
        this.messages = FXCollections.observableArrayList();
    }

    public void addMessage(String msg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messages.add(msg);
            }
        });
    }

    public ObservableList<String> getMessageList(){
        return this.messages;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}