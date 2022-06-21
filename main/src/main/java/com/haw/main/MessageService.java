package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
/**
 *Message service responsible for sending and receiving messages
 */
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
        /**
         * method for starting a thread that sends a message to all connected sockets in the writers list
         * @param message Message to be sent
         */
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
        /**
         * method for starting a thread for each reader which reads the incoming messages
         * @param reader The buffered reader from which to read in messages
         */
        Thread t = new Thread(() -> {
            try {
                while (isRunning) {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        service.receiveMessage(msg);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnect");
            }
        });
        t.setDaemon(true);
        t.start();
    }
}


