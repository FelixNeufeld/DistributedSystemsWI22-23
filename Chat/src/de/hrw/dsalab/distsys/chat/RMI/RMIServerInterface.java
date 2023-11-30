package de.hrw.dsalab.distsys.chat.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    public String getMessage() throws RemoteException;
    public  String sendMessage(String message) throws RemoteException;

}
