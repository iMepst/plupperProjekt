package com.haw.main;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ConnectionClient implements IConnectionService{

    private Boolean isRunning;
    private String host;
    private int port;
    private LinkedList<BufferedWriter> writers;
    private LinkedList<BufferedReader> readers;

    public ConnectionClient(int port, Boolean isRunning, LinkedList<BufferedWriter> writers, LinkedList<BufferedReader> readers, String host){
        this.port = port;
        this.isRunning = isRunning;
        this.writers = writers;
        this.readers = readers;
        this.host = host;
        //this.presenter = presenter;
    }


    public void start() {
        new Thread(() -> {
            System.out.println("Starting client");

            try
             {
                 Socket socket = new Socket(host, port);
                System.out.println("Client connected to " + socket.getRemoteSocketAddress());
                BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writers.add(bwriter);
                readers.add(breader);



//                while (isRunning) {
//                    String msg = reader.readLine();
//                    if (msg != null) {
//                        controller.receiveMessage(msg);
//                    }
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
        System.out.println("Closing client");
    }
}
