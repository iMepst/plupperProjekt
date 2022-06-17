package com.haw.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ConnectionServer implements IConnectionService {

    private final int port;
    private Boolean isRunning;
    private LinkedList<BufferedWriter> writers;
    private LinkedList<BufferedReader> readers;

    private IMessageService messageService;
    //private SessionPresenter presenter;

    public ConnectionServer(int port, Boolean isRunning, LinkedList<BufferedWriter> writers, LinkedList readers, IMessageService messageService) {
        this.port = port;
        this.isRunning = isRunning;
        this.writers = writers;
        this.readers = readers;
        //this.presenter = presenter;
        this.messageService = messageService;
    }

    public void start() {
        new Thread(() -> {
            System.out.println("Starting server");
            try (ServerSocket ssocket = new ServerSocket(port)) {
                System.out.println("Server listening on port " + port);
                while (isRunning) {
                    Socket csocket = ssocket.accept();
                    System.out.println("Connection from " + csocket.getRemoteSocketAddress());
                    handleConnection(csocket);
                }
            } catch (IOException e) { /* LineUnavailableException für Audio*/
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
        System.out.println("Closing server");
    }

    public void handleConnection(Socket socket) {
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writers.add(writer);
                readers.add(reader);

                messageService.receiveMessage(reader);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
