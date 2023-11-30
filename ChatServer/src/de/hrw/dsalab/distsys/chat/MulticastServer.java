package de.hrw.dsalab.distsys.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {

    public void startListening(){
        try(MulticastSocket soc = new MulticastSocket(1337)){
            byte[] buff = new byte[256];
            DatagramPacket packet = new DatagramPacket(buff,buff.length);
            soc.joinGroup(InetAddress.getByName("239.1.2.3"));

            while(true){
                //Funktioniert Lokal nicht, da nicht alle Nutzer den selben Port verwenden können
                soc.receive(packet);
                System.out.println("Received");
                soc.send(packet);
                System.out.println("Send");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
