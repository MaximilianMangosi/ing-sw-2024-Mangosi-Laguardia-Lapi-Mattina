package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientmessages.ClientMessage;
import it.polimi.ingsw.messages.exceptionmessages.ExceptionMessage;
import it.polimi.ingsw.messages.servermessages.HandMessage;
import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Controller controller;
    private final ViewUpdater viewUpdater;


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

    /**
     * sends a SuccessMessage or ExceptionMessage to the client as reply to his command
     * @param msg the message sent
     * @throws IOException when a connection problem occurss
     */
    public void answerClient(ServerMessage msg) throws IOException {
        synchronized (output){
            output.writeObject(msg);
        }
    }
    /**
     * Sends a message to all user containing a view's update, using viewUpdater
     * @param msg the Server Message to send
     * @throws IOException when a connection problem occurs
     */
    public void broadCast (ServerMessage msg) throws IOException {
        //ASSUMPTION : ONLY ONE GAME CAN BE HOSTED AT ONCE ON THE SERVER
        //TODO: implement multiple parallel answers to different players in different games
        boolean Continue = true;
        System.out.println("first try");
        while(Continue){
            Continue=viewUpdater.sendAll(msg);
        }
    }
    public Controller getController(){
        return this.controller;
    }

    public Map<UUID,ObjectOutputStream> getAllClients(){
        return viewUpdater.getClients();
    }

    /**
     * Sends a message to a user containing a view's update, using viewUpdater
     * @param id the user's identifier
     * @param message the Server Message to send
     * @throws IOException when a connection problem occurs
     */

    public void sendTo(UUID id, ServerMessage message) throws IOException {
        viewUpdater.sendTo(message, id);
    }
}
