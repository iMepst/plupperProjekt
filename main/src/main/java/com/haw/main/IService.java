package com.haw.main;

public interface IService{

    public void start();
    public void stop();
    public void sendMessage(String msg);
    public void receiveMessage(String msg);

    public void micSwitch();
}
