package com.haw.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ConnectionService {

    int port;
    String host;
    Boolean isRunning;

    ServerSocket serverSocket;
    LinkedList<BufferedReader> reader;
    LinkedList<BufferedWriter> writer;

    MessageService messageService;

    public ConnectionService(String host, int port, Boolean isRunning, LinkedList<BufferedReader> reader, LinkedList<BufferedWriter> writer, MessageService messageService){
        this.port = port;
        this.isRunning = isRunning;
        this.reader = reader;
        this.writer = writer;
        this.host = host;
        this.messageService = messageService;
    }

    public ConnectionService(int port, Boolean isRunning, LinkedList<BufferedReader> reader, LinkedList<BufferedWriter> writer, MessageService messageService) {
        this.port = port;
        this.isRunning = isRunning;
        this.reader = reader;
        this.writer = writer;
        this.messageService = messageService;
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){
        //wartet auf eingehende Verbindungen auf dem Port

        new Thread(() -> {
            try {
                while (isRunning) {
                    Socket ssocket = serverSocket.accept();
                    System.out.println("Connection from " + ssocket.getRemoteSocketAddress());
                    establishConnection(ssocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void connectToServer(int port, String host){
        //verbindet sich mit Server über den Port
        try {
            Socket csocket = new Socket(host, port);
            System.out.println("Client connected to " + csocket.getRemoteSocketAddress());
            establishConnection(csocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void establishConnection(Socket socket){
        //erstellt einen neuen Thread, der den Socket verwendet
        //erstellt Reader und Writer
        new Thread( () -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.writer.add(writer);
                this.reader.add(reader);

                messageService.receiveMessage(reader);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void disconnectFromServer(){
        //trennt die Verbindung mit dem Server
        isRunning = false;
        System.out.println("Closing server");
        writer.forEach( w -> {
            try {
                w.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        reader.forEach(w -> {
            try {
                w.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
