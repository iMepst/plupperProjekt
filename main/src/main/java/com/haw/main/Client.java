package com.haw.main;

import java.io.*;
import java.net.Socket;

public class Client {

    private SessionPresenter controller;
    private String host;
    private int port;
    private Boolean isRunning;

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public Client(SessionPresenter controller){
        host = "localhost";
        port = 50005;
        isRunning = true;
        this.controller = controller;
    }

    public void start() {
        new Thread() {
            @Override
            public void run(){
                System.out.println("Starting client");
                try(Socket socket = new Socket(host, port);
                    BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader breader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ){
                    System.out.println("Client connected to " + socket.getRemoteSocketAddress());
                    writer = bwriter;
                    reader = breader;

                    while (isRunning){
                        String msg = reader.readLine();
                        System.out.println(msg);
                    }


                }
                catch (IOException e){

                }
            }

        }.start();
    }

    public void sendMessage(String msg){
        new Thread(){
            @Override
            public void run(){
                try {
                    writer.write(msg);
                    writer.flush();
                    System.out.println("Message sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}