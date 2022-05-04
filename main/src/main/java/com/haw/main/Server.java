package com.haw.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;
    private Boolean isRunning;
    private SessionPresenter controller;

    public Server(SessionPresenter controller){
        port = 50005;
        isRunning = true;
        this.controller = controller;
    }


    public void start() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Starting server");
                try (ServerSocket ssocket = new ServerSocket(port)) {
                    System.out.println("Server listening on port " + port);
                    Socket csocket = ssocket.accept();
                    System.out.println("Connection from " + csocket.getRemoteSocketAddress());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
                    while (isRunning) {
                        String msg = reader.readLine();
                        System.out.println("Message received");
                        controller.receiveMessage(msg);
                    }
                }
                catch (IOException e) {
                }
            }
        }.start();

    }
}