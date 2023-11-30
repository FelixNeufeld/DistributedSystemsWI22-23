package de.hrw.dsalab.distsys.chat.RMI;
import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;

import de.hrw.dsalab.distsys.chat.RMI.RMIServer;
import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIKeyboardListener implements InputListener{
    JTextArea textArea;
    String nick;

    Registry registry;

    RMIServerInterface chatServer;
    public RMIKeyboardListener(JTextArea textArea, String nick) throws NotBoundException, RemoteException {
        this.textArea = textArea;
        this.nick = nick;
        this.registry = LocateRegistry.getRegistry("localhost");
        chatServer = (RMIServerInterface) registry.lookup("chatServer");
    }
    @Override
    public void inputReceived(String str) {
        try{
            String message = "<" + nick + "> " + str + System.getProperty("line.separator");
            chatServer.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
