package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Server implements IService {

    private IPresenter presenter;
    private MessageService messageService;
    private ConnectionService connectionService;
    private AudioService audioService;

    private Boolean isRunning;
    private int port;
    private LinkedList<BufferedReader> readers;
    private LinkedList<BufferedWriter> writers;

    public Server(SessionPresenter presenter) {
        this.presenter = presenter;
        this.isRunning = true;
        this.port = 50005;
        this.readers = new LinkedList<>();
        this.writers = new LinkedList<>();
    }

    public void start(){
       // new Thread(() -> {
            System.out.println("Starting server");
            messageService = new MessageService(isRunning, writers, this);

            connectionService = new ConnectionService(port, isRunning, readers, writers, messageService);
            connectionService.listen();

            audioService = new AudioService(isRunning, port);
            audioService.start();
        //}).start();
    }

    public void stop() {
        System.out.println("Closing server");
        isRunning = false;
        connectionService.disconnect();
    }

    public void micSwitch(){
        audioService.micSwitch();
    }

    @Override
    public void sendMessage(String msg) {
       messageService.broadcastMessage(msg);
    }

    @Override
    public void receiveMessage(String msg){
        sendMessage(msg);
        presenter.receiveMessage(msg);
    }
}
