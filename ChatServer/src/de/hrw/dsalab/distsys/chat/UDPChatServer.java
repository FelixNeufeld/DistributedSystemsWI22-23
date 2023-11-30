package de.hrw.dsalab.distsys.chat;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPChatServer{
    private List<Integer> ports;

    public UDPChatServer(){
        this.ports = new ArrayList<>();
    }

    public void startListening(){
        while(true) {
            try (DatagramSocket serverSocket = new DatagramSocket(1337)) {
                byte[] receiveData = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);
                if(!ports.contains(receivePacket.getPort())){
                    ports.add(receivePacket.getPort());
                }

                System.out.println("Message received: " + new String(receiveData));

                for (Integer port : ports) {
                    if (receivePacket.getPort() != port) {
                        DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getData().length, InetAddress.getByName("192.168.2.100") ,port);
                        serverSocket.send(sendPacket);
                        System.out.println("Send Data to: " + port);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}