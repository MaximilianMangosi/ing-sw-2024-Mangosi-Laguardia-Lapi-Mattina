package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.clientmessages.ClientMessage;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.view.ViewRMIContainer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable{
    private final Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final ViewRMIContainer viewContainer;
    private Controller controller;
    private final ConcurrentHashMap<UUID,ViewUpdater> viewUpdaterMap;
    private  ViewUpdater myViewUpdater;

    /**
     *constructor of class ClientHandler
     * @param c the client socket
     * @param viewContainer contains all game's view
     */
    ClientHandler (Socket c, ViewRMIContainer viewContainer,ConcurrentHashMap<UUID,ViewUpdater> viewUpdaterMap) throws RemoteException {
        this.client=c;
        this.viewContainer = viewContainer;
        this.viewUpdaterMap=viewUpdaterMap;

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
            answerClient( new WaitingGamesMessage(viewContainer.getJoinableGames()));
        }catch (IOException e){
            System.out.println("Couldn't connect to "+client.getInetAddress());
        }
        System.out.println("Connected to "+client.getInetAddress());

        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("client" + client.getInetAddress() + " connection dropped\n");
        }

        try{
            client.close();
        } catch (IOException ignored) {}

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
        myViewUpdater.sendAll(msg);
    }

    /**
     * getter of the game controller
     * @return the controller
     */
    public Controller getController(){
        return this.controller;
    }

    public Map<UUID,ObjectOutputStream> getAllClients(){
        return myViewUpdater.getClients();
    }

    /**
     * Sends a message to a user containing a view's update, using viewUpdater
     * @param id the user's identifier
     * @param message the Server Message to send
     * @throws IOException when a connection problem occurs
     */

    public void sendTo(UUID id, ServerMessage message) throws IOException {
        myViewUpdater.sendTo(message, id);
    }

    public void removeClient(UUID myID) {
        myViewUpdater.removeClient(myID);
    }

    public ViewRMIContainer getViewContainer() {
        return viewContainer;
    }

    public void setController(Controller c) {
        controller=c;
    }
    
    public void setMyViewUpdater(UUID gameID){
        myViewUpdater=viewUpdaterMap.get(gameID);
        if(myViewUpdater==null){
            myViewUpdater=new ViewUpdater();
            addViewUpdater(gameID,myViewUpdater);
            new RMIToSocketDispatcher(controller,myViewUpdater).start();
        }
    }

    public void addViewUpdater(UUID gameID,ViewUpdater viewUpdater) {
        viewUpdaterMap.put(gameID,viewUpdater);
        myViewUpdater=viewUpdater;
    }

    public ViewUpdater getViewUpdater() {
        return myViewUpdater;
    }
}
