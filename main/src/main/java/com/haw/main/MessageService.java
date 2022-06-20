package com.haw.main;

import java.io.*;
import java.util.LinkedList;

public class MessageService {

    private Boolean isRunning;
    private SessionPresenter presenter;
    private LinkedList<BufferedWriter> writers;
    private IService service;

    public MessageService(Boolean isRunning, SessionPresenter presenter, LinkedList writers, IService service) {
        this.isRunning = isRunning;
        this.writers = writers;
        this.service = service;
    }

    public void broadcastMessage(String message) {
        new Thread(() -> {
            for (BufferedWriter writer : writers) {
                try {
                    System.out.println("Message broadcasted");
                    writer.write(message + "\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void receiveMessage(BufferedReader reader) {
        new Thread(() -> {
            while (isRunning) {
                try {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        service.receiveMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


