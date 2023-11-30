package de.hrw.dsalab.distsys.chat.UDP.Broadcast;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastKeyboardListener implements InputListener {
    DatagramSocket soc;
    private JTextArea textArea;
    private String nick;

    public BroadcastKeyboardListener(JTextArea textArea, String nick, int port){
        this.textArea = textArea;
        this.nick = nick;
        try{
            soc = new DatagramSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void inputReceived(String str) {
        String message = "<" + nick + "> " + str + System.getProperty("line.separator");

        try {
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,
                    InetAddress.getByName("192.168.2.255"),1337);
            soc.send(sendPacket);
            System.out.println("Message send: " + message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public DatagramSocket getSocket(){
        return this.soc;
    }
}
