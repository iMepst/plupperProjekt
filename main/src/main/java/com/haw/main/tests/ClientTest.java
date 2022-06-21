package com.haw.main.tests;

import com.haw.main.Client;
import com.haw.main.SessionPresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client client;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        client = new Client(new SessionPresenter());
        assertNotNull(client);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void start() {
        client.start();
    }

    @Test
    void stop() {
    }

    @Test
    void micSwitch() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void receiveMessage() {
    }
}