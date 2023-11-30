package de.hrw.dsalab.distsys.chat.RMI;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        try {
            RMIServerInterface instance = new RMIServer();
            RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(instance, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("chatServer", stub);
            System.out.println("Server started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}