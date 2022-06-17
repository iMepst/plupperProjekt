package com.haw.main;

import java.io.BufferedReader;

public interface IMessageService {

    public void sendMessage(String msg);
    public void receiveMessage(BufferedReader reader);
}
