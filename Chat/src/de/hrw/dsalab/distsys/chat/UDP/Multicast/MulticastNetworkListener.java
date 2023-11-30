package de.hrw.dsalab.distsys.chat.UDP.Multicast;

import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastNetworkListener implements NetworkListener {
    MulticastSocket soc;
    JTextArea textArea;
    String nick;

    public MulticastNetworkListener(JTextArea textArea, String nick, MulticastSocket socket){
        this.textArea = textArea;
        this.nick = nick;
        try{
            soc = socket;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void messageReceived(String msg) {
        byte[] buff = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(buff, buff.length);
        while(true) {
            try{
                soc.receive(receivePacket);
                System.out.println("received");
                String message = new String(receivePacket.getData());
                textArea.append(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
