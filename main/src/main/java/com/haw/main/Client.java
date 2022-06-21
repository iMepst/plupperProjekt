package com.haw.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.LinkedList;

public class Client implements IService {

        private IPresenter presenter;
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

        @Override
        public void start() {
                new Thread(() -> {
                        System.out.println("Starting client");
                        messageService = new MessageService(isRunning, writers, this);

                        connectionService = new ConnectionService(host, port, isRunning, readers, writers, messageService);
                        connectionService.connect();

                        audioService = new AudioService(isRunning, port);
                        audioService.receiveAudio();
                }).start();
        }

        @Override
        public void stop() {
                System.out.println("Closing client");
                isRunning = false;
                connectionService.disconnect();
        }

        @Override
        public void micSwitch() {
                return;
        }

        @Override
        public void sendMessage(String msg) {
                messageService.broadcastMessage(msg);
        }

        @Override
        public void receiveMessage(String msg) {
                presenter.receiveMessage(msg);
        }
}