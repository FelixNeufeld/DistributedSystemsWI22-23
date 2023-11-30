package de.hrw.dsalab.distsys.chat.RMI;


import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;

import de.hrw.dsalab.distsys.chat.RMI.RMIServer;
import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMINetworkListener implements NetworkListener {
    JTextArea textArea;
    String nick;

    Registry registry;

    RMIServerInterface chatServer;

    String currentMessage;

    public RMINetworkListener(JTextArea textArea, String nick) throws RemoteException, NotBoundException {
        this.textArea = textArea;
        this.nick = nick;
        currentMessage = "";

        this.registry = LocateRegistry.getRegistry("localhost");
        chatServer = (RMIServerInterface) registry.lookup("chatServer");
    }
    @Override
    public void messageReceived(String msg) {
        try {
            if (currentMessage != null && !currentMessage.equals(chatServer.getMessage())) {
                currentMessage = chatServer.getMessage();
                textArea.append(currentMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
