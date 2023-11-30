package de.hrw.dsalab.distsys.chat;

import java.net.MulticastSocket;

public class Main {
    public static void main(String[] arc){
        MulticastServer cs = new MulticastServer();
        cs.startListening();
    }

}
