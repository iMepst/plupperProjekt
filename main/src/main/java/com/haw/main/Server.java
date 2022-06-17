package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Server implements IService {

    private int port;
    private LinkedList<BufferedReader> readers;
    private LinkedList<BufferedWriter> writers;
    private SessionPresenter presenter;
    private Boolean isRunning;

    private IMessageService messageService;
    private IConnectionService connectionService;
    public IAudioService audioService;

    public Server(SessionPresenter presenter) {
        this.port = 50005;
        this.readers = new LinkedList<>();
        this.writers = new LinkedList<>();
        this.presenter = presenter;
        this.isRunning = true;
    }

    public void start(){
        new Thread(() -> {
            System.out.println("Starting server");
            messageService = new MessageService(isRunning, writers, readers, presenter);
            connectionService = new ConnectionServer(port, isRunning, writers, readers, messageService);
            connectionService.start();
            audioService = new AudioService(isRunning, port);
            audioService.start();
        }).start();

    }

    public void stop() {
        isRunning = false;
        System.out.println("Closing server");
        writers.forEach( w -> {
            try {
                w.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        readers.forEach(w -> {
            try {
                w.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void micSwitch(){
        audioService.micSwitch();
    }

    @Override
    public void sendMessage(String msg) {
        return;
    }

    @Override
    public void receiveMessage(BufferedReader reader) {
        return;
    }
}
