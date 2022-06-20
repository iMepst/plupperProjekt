package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MessageService {

    private Boolean isRunning;
    private SessionPresenter presenter;

    public MessageService(Boolean isRunning, LinkedList<BufferedWriter> writers, LinkedList<BufferedReader> readers, SessionPresenter presenter) {
        this.isRunning = isRunning;
        this.writers = writers;
        this.readers = readers;
        this.presenter = presenter;

        //receiveMessage(readers.getFirst());
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            try {
                System.out.println("Message sent");
                writer.write(message + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        presenter.receiveMessage(message);
    }

    public void receiveMessage(BufferedReader reader) {
        new Thread(() -> {
            while (isRunning) {
                try {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        sendMessage(msg);
                        presenter.receiveMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


