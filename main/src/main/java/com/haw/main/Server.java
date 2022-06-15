package com.haw.main;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import javax.sound.sampled.*;

public class Server implements IConnection {

    private final int port;
    private Boolean isRunning;
    private SessionPresenter controller;
    private LinkedList<BufferedWriter> writers;

    /* ---------------- Audio ---------------*/
    private TargetDataLine line;
    private DatagramPacket dgp;
    private AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    private float rate = 48000.0f;
    private int channels = 1;
    private int sampleSize = 16;
    private boolean bigEndian = false;
    private InetAddress addr;
    boolean micOpen = true;
    /* ---------------- Audio ---------------*/

    public Server(SessionPresenter controller){
        port = 50005;
        isRunning = true;
        this.controller = controller;
        writers = new LinkedList<>();
    }


    public void start() {
        new Thread( () -> {
            System.out.println("Starting server");
            try (ServerSocket ssocket = new ServerSocket(port)) {
                System.out.println("Server listening on port " + port);
                /* ---------------- Audio ---------------*/

                //displayMixerInfo();
                System.setProperty("java.net.preferIPv4Stack", "true");
                AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize/8) * channels, rate, bigEndian);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if(!AudioSystem.isLineSupported(info)) {
                    System.out.println("Data line not supported!");
                }

                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                byte[] data = new byte[4096];

                addr = InetAddress.getLocalHost();
                MulticastSocket multiSocket = new MulticastSocket();

                /* ---------------- Audio ---------------*/
                while (isRunning) {
                    Socket csocket = ssocket.accept();
                    System.out.println("Connection from " + csocket.getRemoteSocketAddress());
                    handleConnection(csocket);
                    /* ---------------- Audio ---------------*/
                    sendAudio(multiSocket,data);
                    /* ---------------- Audio ---------------*/
                }
            }
            catch (IOException | LineUnavailableException e) { /* LineUnavailableException für Audio*/
            }
        }).start();
    }

    public void stop(){
        isRunning = false;
        System.out.println("Closing server");
    }

    public void sendMessage(String message){
        new Thread(){
            @Override
            public void run(){
                for (BufferedWriter writer : writers){
                    try {
                        writer.write(message + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void handleConnection(Socket socket) {
        new Thread(() -> {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
            ) {
                writers.add(writer);
                while (isRunning) {
                    String msg = reader.readLine();
                    if (msg != null) {
                        System.out.println("Message received");
                        sendMessage(msg);
                        controller.receiveMessage(msg);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /* ---------------- Audio ---------------*/
    public void sendAudio(MulticastSocket multiSocket, byte[] data){

        try{
            while (true){
                if(this.micOpen){
                    line.read(data, 0, data.length);
                    dgp = new DatagramPacket(data, data.length, addr, port);
                    multiSocket.send(dgp);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void displayMixerInfo()
    {
        Mixer.Info [] mixersInfo = AudioSystem.getMixerInfo();

        System.out.println(mixersInfo[0]);

        for (Mixer.Info mixerInfo : mixersInfo)
        {
            System.out.println("Mixer: " + mixerInfo.getName());

            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info [] sourceLineInfo = mixer.getSourceLineInfo();
            for (Line.Info info : sourceLineInfo)
            {
                showLineInfo(info);
            }

            Line.Info [] targetLineInfo = mixer.getTargetLineInfo();
            for (Line.Info info : targetLineInfo)
            {
                showLineInfo(info);
            }
        }
    }
    private static void showLineInfo(Line.Info lineInfo)
    {
        System.out.println("  " + lineInfo.toString());

        if (lineInfo instanceof DataLine.Info)
        {
            DataLine.Info dataLineInfo = (DataLine.Info)lineInfo;

            AudioFormat [] formats = dataLineInfo.getFormats();
            for (AudioFormat format : formats)
            {
                System.out.println("    " + format.toString());
            }
        }
    }

    public void micSwitch(){
        this.micOpen = !micOpen;
        System.out.println(this.micOpen);
    }
    /* ---------------- Audio ---------------*/





}