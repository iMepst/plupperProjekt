package com.haw.main;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class Client implements IConnection{

    private SessionPresenter controller;
    private String host;
    private int port;
    private Boolean isRunning;

    private BufferedWriter writer;
    private BufferedReader reader;

    /* ---------------- Audio ---------------*/
    private static AudioInputStream ais;
    private static AudioFormat format;
    private static float rate = 48000.0f;


    private static DataLine.Info dataLineInfo;
    private static SourceDataLine sourceDataLine;

    private boolean speakerOn = true;

    /* ---------------- Audio ---------------*/

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

                /* ---------------- Audio ---------------*/
                System.setProperty("java.net.preferIPv4Stack", "true");
                /* ---------------- Audio ---------------*/

                try(Socket socket = new Socket(host, port);
                    BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader breader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ){
                    System.out.println("Client connected to " + socket.getRemoteSocketAddress());
                    writer = bwriter;
                    reader = breader;

                    /* ---------------- Audio ---------------*/

                    InetAddress group = InetAddress.getLocalHost();
                    MulticastSocket mSocket = new MulticastSocket(port);
                    System.out.println(group);
                    joinGroup(group);

                    byte[] receiveData = new byte[4096];

                    format = new AudioFormat(rate, 16, 1, true, false);

                    dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                    sourceDataLine.open(format);
                    sourceDataLine.start();

                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    ByteArrayInputStream baiss = new ByteArrayInputStream(receivePacket.getData());

                    /* ---------------- Audio ---------------*/

                    while (isRunning){
                        /* ---------------- Audio ---------------*/
                        receiveAudio(mSocket, receivePacket, baiss);
                        /* ---------------- Audio ---------------*/
                        String msg = reader.readLine();
                        if (msg != null) {
                            controller.receiveMessage(msg);
                        }
                    }

                }
                catch (IOException | LineUnavailableException e){
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public void stop(){
        isRunning = false;
        System.out.println("Closing client");
    }

    public void sendMessage(String msg){
        new Thread(){
            @Override
            public void run(){
                try {
                    if(msg != ""){
                        writer.write(msg + "\n");
                        writer.flush();
                        System.out.println("Message sent");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /* ---------------- Audio ---------------*/
    public static void toSpeaker(byte soundbytes[]){
        try{
            //System.out.println("At Speaker");
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Deprecated(since = "14")
    public void joinGroup(java.net.InetAddress mcastaddr)
            throws java.io.IOException {

    }

    public void receiveAudio(MulticastSocket mSocket, DatagramPacket receivePacket, ByteArrayInputStream baiss){
        new Thread(){
            @Override
            public void run(){
                while(speakerOn){
                    try{
                        //System.out.println("Receive Audio");
                        mSocket.receive(receivePacket);
                        ais = new AudioInputStream(baiss, format, receivePacket.getLength());
                        toSpeaker(receivePacket.getData());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    /* ---------------- Audio ---------------*/


}