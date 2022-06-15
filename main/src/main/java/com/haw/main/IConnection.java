package com.haw.main;

public interface IConnection {

    public void start();
    public void stop();
    public void sendMessage(String message);
    public void micSwitch();
}
