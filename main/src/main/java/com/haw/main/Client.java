package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Client implements IService {

        private SessionPresenter presenter;
        private MessageService messageService;
        private ConnectionService connectionService;
        private AudioService audioService;

        private String host;
        private int port;
        private Boolean isRunning;

        private LinkedList<BufferedReader> readers;
        private LinkedList<BufferedWriter> writers;


        public Client(SessionPresenter presenter) {
                this.host = "localhost";
                this.port = 50005;
                this.isRunning = true;
                this.presenter = presenter;
                writers = new LinkedList<>();
                readers = new LinkedList<>();

        }

        public void start() {
                new Thread(() -> {
                        System.out.println("Starting client");
                        messageService = new MessageService(isRunning, presenter, writers, this);

                        connectionService = new ConnectionService(host, port, isRunning, readers, writers, messageService);
                        connectionService.connectToServer(port, host);

                        audioService = new AudioService(isRunning, port);
                        audioService.receiveAudio();
                }).start();
        }

        public void stop() {
                System.out.println("Closing client");
                connectionService.disconnectFromServer();
                isRunning = false;

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

        @Override
        public void micSwitch() {
                return;
        }


        public void sendMessage(String msg) {
                messageService.broadcastMessage(msg);
        }

        public void receiveMessage(String msg) {
                presenter.receiveMessage(msg);
        }
}