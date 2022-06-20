package com.haw.main;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import javax.sound.sampled.*;

public class Client implements IService, IMessageService {

        private SessionPresenter presenter;
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
                writers = new LinkedList<BufferedWriter>();
                readers = new LinkedList<BufferedReader>();

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

        @Override
        public void sendMessage(String msg) {
                messageService.sendMessage(msg);
        }

        @Override
        public void receiveMessage(BufferedReader reader) {
                messageService.receiveMessage(reader);
        }
}