package com.haw.main;

import java.io.*;
import java.util.LinkedList;

public class MessageService {

    private Boolean isRunning;
    private LinkedList<BufferedWriter> writers;
    private IService service;

    public MessageService(Boolean isRunning, LinkedList writers, IService service) {
        this.isRunning = isRunning;
        this.writers = writers;
        this.service = service;
    }

    public void broadcastMessage(String message) {
        Thread t = new Thread(() -> {
            for (BufferedWriter writer : writers) {
                try {
                    System.out.println("Message broadcasted");
                    writer.write(message + "\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }


    public void receiveMessage(BufferedReader reader) {
        Thread t = new Thread() {
        @Override
        public void run () {
            while (isRunning) {
                try {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        service.receiveMessage(msg);
                    }
                } catch (IOException e) {
                    isRunning = false;
                    service.stop();
                    break;
                }
            }
        }};
        t.setDaemon(true);
        t.start();
    }
}


