package com.haw.main;

import java.io.*;
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


