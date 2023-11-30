package de.hrw.dsalab.distsys.chat.RMI;

import java.rmi.RemoteException;

public class RMIServer implements RMIServerInterface {

    private String lastMessage = "";

    @Override
    public String getMessage() throws RemoteException {
        System.out.println("Message send: " + lastMessage);
        return lastMessage;
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        System.out.println("Message received: " + message);
        this.lastMessage = message;
        return message;
    }
}
