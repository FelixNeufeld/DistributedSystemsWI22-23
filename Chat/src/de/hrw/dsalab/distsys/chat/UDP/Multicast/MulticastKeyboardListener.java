package de.hrw.dsalab.distsys.chat.UDP.Multicast;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastKeyboardListener implements InputListener {
    private MulticastSocket soc;
    private JTextArea textArea;
    private String nick;

    public MulticastKeyboardListener(JTextArea textArea, String nick, int port){
        this.textArea = textArea;
        this.nick = nick;
        try{
            soc = new MulticastSocket(port);
            soc.joinGroup(InetAddress.getByName("239.1.2.3"));
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

    public MulticastSocket getSocket(){
        return this.soc;
    }
}
