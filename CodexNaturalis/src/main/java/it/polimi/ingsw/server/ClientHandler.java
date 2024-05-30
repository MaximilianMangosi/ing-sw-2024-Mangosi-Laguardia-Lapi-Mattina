package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.clientmessages.ClientMessage;
import it.polimi.ingsw.messages.servermessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Controller controller;
    private final ViewUpdater viewUpdater;

    /**
     *constructor of class ClientHandler
     * @param c the client socket
     * @param controller game controller
     * @param viewUpdater object that contains a list of clientHandlers
     */
    ClientHandler (Socket c,Controller controller,ViewUpdater viewUpdater){
        this.client=c;
        this.controller=controller;
        this.viewUpdater = viewUpdater;
    }

    /**
     * main thread logic, calls handleConnection
     * @author Giogio Mattina
     */
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
            new Thread(this::RMIToSocketDispatcher).start();
            handleConnection();
        } catch (IOException e) {
            System.out.println("client" + client.getInetAddress() + " connection dropped\n");
        }

        try{
            client.close();
        } catch (IOException ignored) {}

    }

    /**
     * Retrieves a message from Remote view and send to the Socket clients
     * @author Giuseppe Laguardia
     */
    private void RMIToSocketDispatcher(){
        while (true) {
            try {
                if(shouldStop())
                    break;
                ServerMessage newMsg=controller.retrieveMessage();
                if(newMsg instanceof GameStartMessage) {
                    for (UUID id : getAllClients().keySet()) {
                        sendTo(id, new HandMessage(controller.getPlayersHands().get(id)));
                        sendTo(id, new GoalOptionsMessage(controller.getGoalOptions().get(id)));
                        sendTo(id, new StarterCardMessage(controller.getPlayersStarterCards().get(id)));
                    }
                    viewUpdater.sendAll(newMsg);
                } else if (newMsg instanceof UpdateChatMessage) {
                    UUID receiver = ((UpdateChatMessage) newMsg).getReceiver();
                    sendTo(receiver,newMsg);
                } else
                    viewUpdater.sendAll(newMsg);

            } catch (IOException e) {
                System.out.println("RMIToSocketDispatcher stopped");
            }
        }
    }

    /**
     * check if a RMIToSocketDispatcher thread should continue
     * @return true if there is at least a player using RMI, false otherwise
     * @throws RemoteException if a connection error occurs
     */
    private boolean shouldStop() throws RemoteException {
        // if there is player in game are more than the players using socket then at least one of them is using RMI and the thread must continue
        int numOfPlayersInGame= controller.getPlayersList().size();
        int numOfSocketPlayers= viewUpdater.getClients().size();
        return  controller.getView().isGameStarted() && numOfPlayersInGame <= numOfSocketPlayers;
    }

    /**
     * reads from input and calls processMessage on the Message object
     * @author Giorgio Mattina
     * @throws IOException
     */
    private void handleConnection() throws IOException{
        try{
            while(true){
                Object next = input.readObject();
                ClientMessage cmd =(ClientMessage) next;
                cmd.processMessage(this);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid stream from client");
            System.exit(1);
        }
    }

    /**
     * sends a SuccessMessage or ExceptionMessage to the client as reply to his command
     * @param msg the message sent
     * @throws IOException when a connection problem occurss
     */
    public void answerClient(ServerMessage msg) throws IOException {
        output.writeObject(msg);
    }
    /**
     * Sends a message to all user containing a view's update, using viewUpdater
     * @param msg the Server Message to send
     * @throws IOException when a connection problem occurs
     */
    public void broadCast (ServerMessage msg) throws IOException {
        //ASSUMPTION : ONLY ONE GAME CAN BE HOSTED AT ONCE ON THE SERVER
        //TODO: implement multiple parallel answers to different players in different games
        viewUpdater.sendAll(msg);
    }

    /**
     * getter of the game controller
     * @return
     */
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

    public void removeClient(UUID myID) {
        viewUpdater.removeClient(myID);
    }
}
