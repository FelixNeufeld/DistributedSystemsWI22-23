package de.hrw.dsalab.distsys.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer{
    private static final int PORT = 1337;
    ServerSocket serverSocket;
    private ExecutorService executorService;
    private List<ClientHandler> clients = new ArrayList<>();
    private static int id = 0;

    public ChatServer() {
        //Opening the ServerSocket and opening a thread pool with a max of 10 connections
        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(10);
            clients = new ArrayList<>();
            System.out.println("Server started!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening(){
        try {
            //Endless loop for connecting to the server
            while (true) {
                Socket clientSocket = serverSocket.accept();

                //synchronizing threads
                synchronized (clients){
                    //Debug output
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    //Creating the Client handler and adding them to the List
                    ClientHandler clientHandler = new ClientHandler(clientSocket, id++);
                    clients.add(clientHandler);
                    //Executing run Method of clientHandler in a new Thread
                    executorService.execute(clientHandler);
                }
            }
        } catch (Exception ex) {
            System.out.println("Client has left the chat");
            if(clients.isEmpty()){
                try{
                    serverSocket.close();
                }catch(IOException ioe){
                    System.err.println("Server socket could not be closed");
                }
            }
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        //Sending a Broadcast message to all other clients
        // Every Client has 2 Sockets i and i+1. Every other client is >i+1 or <i+1
        for (ClientHandler client : clients) {
            if ((client.id%2 == 1) && (client.id != sender.id) && (client.id != sender.id+1)) {
                client.sendMessage(message);
            }
        }
    }

    public void disconnectClient(ClientHandler sender){
        for(int i = 0; i < clients.size(); i++){
            if(sender.id == clients.get(i).id || sender.id+1 == clients.get(i).id){
                clients.remove(i);
            }
        }
        try{
            sender.clientSocket.close();
            System.out.println("User disconnected");

        }catch (IOException ioe){
            System.err.println("Closing the socket to sender: " + sender.id + " failed");
        }
    }

    public void stopServer(){
        try{
            System.out.println("Server shutdown");
            for(ClientHandler clients : clients){
                clients.clientSocket.close();
                System.out.println("User disconnected");
            }
            serverSocket.close();
        }catch(Exception e){
            System.err.println("There was an error closing the socket");
        }
    }


    class ClientHandler implements Runnable{
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private int id;

        public ClientHandler(Socket clientSocket, int id){
            this.id = id;
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("Failed to create reader or writer");
            }
        }

        //This Method Overrides the run method, so it can run in its own thread
        @Override
        public void run(){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while(true){
                    String receivedMessage = reader.readLine();

                    if(receivedMessage == null){
                        break;
                    }

                    System.out.println(receivedMessage);
                    broadcastMessage(receivedMessage, this);
                }
            }catch(Exception ex){
                disconnectClient(this);
            }
        }
        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}