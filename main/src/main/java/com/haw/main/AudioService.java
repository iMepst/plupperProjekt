package com.haw.main;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;

public class AudioService implements IAudioService {
    private final int port;
    private TargetDataLine line;
    private DatagramPacket dgp;
    private AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    private float rate = 48000.0f;
    private int channels = 1;
    private int sampleSize = 16;
    private boolean bigEndian = false;
    private InetAddress addr;
    private Boolean micOpen = true;
    private Boolean isRunning = true;

    private static AudioInputStream ais;
    private static AudioFormat format;

    private static DataLine.Info dataLineInfo;
    private static SourceDataLine sourceDataLine;

    private boolean speakerOn = true;

    MulticastSocket mSocket;
    DatagramPacket receivePacket;
    ByteArrayInputStream baiss;




    public AudioService(Boolean isRunning, int port) {
        this.port = port;
        this.isRunning = isRunning;
    }

    public void start() {
        new Thread(() -> {
            try {
                System.setProperty("java.net.preferIPv4Stack", "true");
                AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Data line not supported!");
                }
                //Server
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                byte[] data = new byte[4096];

                addr = InetAddress.getLocalHost();
                MulticastSocket multiSocket = new MulticastSocket();


                while (isRunning) {
                    sendAudio(multiSocket, data);
                }
            } catch (IOException | LineUnavailableException e) { /* LineUnavailableException für Audio*/
            }
        }).start();
    }

    @Override
    public void stop() {

    }

    public void sendAudio(MulticastSocket multiSocket, byte[] data) {
        try {
            while (true) {
                if (this.micOpen) {
                    line.read(data, 0, data.length);
                    dgp = new DatagramPacket(data, data.length, addr, port);
                    multiSocket.send(dgp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void toSpeaker(byte[] soundbytes) {
        try {
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveAudio() {
        System.setProperty("java.net.preferIPv4Stack", "true");

        try{
            InetAddress group = InetAddress.getLocalHost();
            MulticastSocket mSocket = new MulticastSocket(port);
            this.mSocket = mSocket;
            System.out.println(group);
            joinGroup(group);

            byte[] receiveData = new byte[4096];

            format = new AudioFormat(rate, 16, 1, true, false);

            dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            this.receivePacket = receivePacket;
            ByteArrayInputStream baiss = new ByteArrayInputStream(receivePacket.getData());
            this.baiss = baiss;
        }catch(IOException | LineUnavailableException e){
            e.printStackTrace();

        }
        new Thread(() -> {
            while (speakerOn) {
                try {
                    mSocket.receive(receivePacket);
                    ais = new AudioInputStream(baiss, format, receivePacket.getLength());
                    toSpeaker(receivePacket.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void micSwitch() {
        this.micOpen = !micOpen;
        System.out.println(this.micOpen);
    }

    @Deprecated(since = "14")
    public void joinGroup(java.net.InetAddress mcastaddr)
            throws java.io.IOException {

    }
}
