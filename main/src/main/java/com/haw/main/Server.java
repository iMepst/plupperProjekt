package com.haw.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server implements IConnection {

    private final int port;
    private Boolean isRunning;
    private SessionPresenter controller;
    private LinkedList<BufferedWriter> writers;

    public Server(SessionPresenter controller){
        port = 50005;
        isRunning = true;
        this.controller = controller;
        writers = new LinkedList<>();
    }


    public void start() {
        new Thread( () -> {
            System.out.println("Starting server");
            try (ServerSocket ssocket = new ServerSocket(port)) {
                System.out.println("Server listening on port " + port);
                while (isRunning) {
                    Socket csocket = ssocket.accept();
                    System.out.println("Connection from " + csocket.getRemoteSocketAddress());
                    handleConnection(csocket);
                }
            }
            catch (IOException e) {
            }
        }).start();
    }

    public void stop(){
        isRunning = false;
    }

    public void sendMessage(String message){
        new Thread(){
            @Override
            public void run(){
                for (BufferedWriter writer : writers){
                    try {
                        writer.write(message + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void handleConnection(Socket socket) {
        new Thread(() -> {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
            ) {
                writers.add(writer);
                while (isRunning) {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        sendMessage(msg);
                        controller.receiveMessage(msg);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



}