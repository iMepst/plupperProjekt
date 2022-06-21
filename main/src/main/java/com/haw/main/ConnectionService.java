package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Connection service class responsible for building and destroying connections
 */

public class ConnectionService {

    private int port;
    private String host;
    private Boolean isRunning;

    private Closeable socket;
    private LinkedList<BufferedReader> reader;
    private LinkedList<BufferedWriter> writer;

    private MessageService messageService;

    public ConnectionService(String host, int port, Boolean isRunning, LinkedList<BufferedReader> reader, LinkedList<BufferedWriter> writer, MessageService messageService){
        this.host = host;
        this.port = port;
        this.isRunning = isRunning;
        this.reader = reader;
        this.writer = writer;
        this.messageService = messageService;
    }

    public ConnectionService(int port, Boolean isRunning, LinkedList<BufferedReader> reader, LinkedList<BufferedWriter> writer, MessageService messageService) {
        this.port = port;
        this.isRunning = isRunning;
        this.reader = reader;
        this.writer = writer;
        this.messageService = messageService;
    }

    public void listen(){
        /**
         * starts a thread that listens for connections on the port specified by the object's field
         */
        Thread t = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                this.socket = serverSocket;
                while (isRunning) {
                    Socket ssocket = serverSocket.accept();
                    System.out.println("Incoming connection from " + ssocket.getRemoteSocketAddress());
                    establishConnection(ssocket);
                }
            } catch (IOException e) {
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void connect(){
        /**
         * connects to the host and port specified by the object's fields
         */
        try {
            Socket csocket = new Socket(host, port);
            this.socket = csocket;
            System.out.println("Connected to " + csocket.getRemoteSocketAddress());
            establishConnection(csocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void establishConnection(Socket socket){
        /**
         * starts a thread that establishes the connection by building the stream objects for reading and writing
         * and starting the receiver thread of the message service object
         * @param socket Socket from which to build the stream objects
         */
        Thread t = new Thread( () -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.writer.add(writer);
                this.reader.add(reader);
                messageService.receiveMessage(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void disconnect(){
        /**
         * method for disconnecting by closing the socket and the readers and writers
         */
        System.out.println("Shutting down connection service");
        writer.forEach( w -> {
            try {
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reader.forEach(w -> {
            try {
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
