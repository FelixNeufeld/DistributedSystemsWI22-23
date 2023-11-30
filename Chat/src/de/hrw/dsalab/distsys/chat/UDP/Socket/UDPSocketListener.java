package de.hrw.dsalab.distsys.chat.UDP.Socket;

import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSocketListener implements NetworkListener {
    DatagramSocket soc;
    JTextArea textArea;
    String nick;

    public UDPSocketListener(JTextArea textArea, String nick, DatagramSocket socket){
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
                String message = new String(receivePacket.getData());
                textArea.append(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
