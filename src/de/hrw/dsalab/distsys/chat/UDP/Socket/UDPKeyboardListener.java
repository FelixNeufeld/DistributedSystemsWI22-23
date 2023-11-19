package de.hrw.dsalab.distsys.chat.UDP.Socket;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPKeyboardListener implements InputListener {
    DatagramSocket soc;
    private JTextArea textArea;
    private String nick;

    public UDPKeyboardListener(JTextArea textArea, String nick){
        this.textArea = textArea;
        this.nick = nick;
        try{
            soc = new DatagramSocket(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void inputReceived(String str) {
        String message = "<" + nick + "> " + str + System.getProperty("line.separator");
        textArea.append(message);

        try {
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,
                    InetAddress.getByName("192.168.2.100"),1337);
            soc.send(sendPacket);
            System.out.println("Message send: " + message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connectMessage(){
        String message = "";
        try {
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,
                    InetAddress.getByName("192.168.2.100"),1337);
            soc.send(sendPacket);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public DatagramSocket getSocket(){
        return this.soc;
    }
}
