package de.hrw.dsalab.distsys.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class BroadcastServer {

    public void startListening(){
        while(true) {
            try (DatagramSocket serverSocket = new DatagramSocket(1337)) {
                byte[] receiveData = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);

                System.out.println("Message received: " + new String(receiveData));

                //Funktioniert nicht Lokal auf dem selben Rechner, da alle Clients unterschiedliche Ports nutzen müssen
                DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getData().length,
                        InetAddress.getByName("192.168.2.255"),1338);
                serverSocket.send(sendPacket);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
