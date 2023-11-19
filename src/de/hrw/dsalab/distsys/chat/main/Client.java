package de.hrw.dsalab.distsys.chat.main;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;
import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;
import de.hrw.dsalab.distsys.chat.Socket.ClientSocketListener;

import javax.swing.*;

public class Client {
    private NetworkListener NetListener;
    private InputListener inListener;

    public Client(InputListener inputListener, NetworkListener networkListener){
        this.NetListener =  networkListener;
        this.inListener = inputListener;
    }

    public void changeToSocketListener(JTextArea textArea, String nick){
        NetListener = new ClientSocketListener(textArea,nick);
    }

    public InputListener getInListener() {
        return inListener;
    }

    public NetworkListener getNetListener() {
        return NetListener;
    }

    public void setInListener(InputListener inListener) {
        this.inListener = inListener;
    }

    public void setNetListener(NetworkListener netListener) {
        NetListener = netListener;
    }
}
