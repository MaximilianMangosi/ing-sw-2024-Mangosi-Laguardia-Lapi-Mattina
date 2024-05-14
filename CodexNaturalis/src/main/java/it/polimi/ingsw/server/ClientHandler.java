package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientmessages.ClientMessage;
import it.polimi.ingsw.messages.exceptionmessages.ExceptionMessage;
import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ClientHandler  implements Runnable{
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Controller controller;
    private ViewUpdater viewUpdater;


    ClientHandler (Socket c,Controller controller,ViewUpdater viewUpdater){
        this.client=c;
        this.controller=controller;
        this.viewUpdater = viewUpdater;
    }

    @Override
    public void run(){
        try{
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        }catch (IOException e){
            System.out.println("Couldn't connect to "+client.getInetAddress());
        }
        System.out.println("Connected to "+client.getInetAddress());

        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("client" + client.getInetAddress() + "connection dropped\n");
        }

        try{
            client.close();
        } catch (IOException ignored) {}

    }

    private void handleConnection() throws IOException{
        try{
            while(true){
                Object next = input.readObject();
                ClientMessage cmd =(ClientMessage) next;
                cmd.processMessage(this);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid stream from client\n");
        }
    }

    public void answerClient(Message msg) throws IOException {
        synchronized (output){
            output.writeObject((Object)msg);
        }
    }
    public void broadCast (ServerMessage msg) throws IOException {
        //ASSUMPTION : ONLY ONE GAME CAN BE HOSTED AT ONCE ON THE SERVER
        //TODO: implement multiple parallel answers to different players in different games
        viewUpdater.send(msg);
    }
    public Controller getController(){
        return this.controller;
    }

    public void addClient (UUID userId, ClientHandler c){
        viewUpdater.addClient(userId,c);
    }
    public Map<UUID,ClientHandler> getAllClients(){
        return viewUpdater.getClients();
    }
}
