package com.haw.main;

public interface IService extends IMessageService {

    public void start();
    public void stop();

    public void micSwitch();
}
