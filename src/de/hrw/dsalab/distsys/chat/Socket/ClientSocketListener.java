package de.hrw.dsalab.distsys.chat.Socket;

import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocketListener implements NetworkListener {
    private JTextArea textArea;
    private String nick;
    boolean connectionShutDown = false;
    Socket socket;

    public ClientSocketListener(JTextArea textArea, String nick) {
        this.textArea = textArea;
        this.nick = nick;

        try {
            socket = new Socket("localhost", 1337);
        } catch (IOException e) {
            connectionShutDown = true;
            System.err.println("Server offline try restarting");
        }
    }
    @Override
    public void messageReceived(String msg) {
        if(connectionShutDown){
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                String receivedMessage = reader.readLine();

                if (receivedMessage == null) {
                    break;
                }

                System.out.println("received: " + receivedMessage);
                //Changing the data in the front end
                textArea.append(receivedMessage + System.getProperty("line.separator"));

            }
        }catch(Exception ex){
            System.out.println("An error occured while receiving a Message, closing socket");
            try{
                connectionShutDown = true;
                socket.close();
            }catch (Exception e){
                System.err.println("error occured while closing the socket");
            }
        }
    }

    public boolean isConnectionClosed(){
        return socket.isClosed();
    }
}

