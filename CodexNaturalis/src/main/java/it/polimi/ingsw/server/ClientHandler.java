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

/**
 * The Runnable handling client messages on port 2323
 */
public class ClientHandler implements Runnable{
    private final Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final ViewRMIContainer viewContainer;
    private Controller controller;
    private final ConcurrentHashMap<UUID,ViewUpdater> viewUpdaterMap;
    private  ViewUpdater myViewUpdater;

    /**
     * @author Giuseppe Laguardia
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
     * @author Giuseppe Laguardia
     * main thread logic, calls handleConnection
     * @author Giogio Mattina
     */
    @Override
    public void run(){
        try{
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            answerClient( new JoinableGamesMessage(viewContainer.getJoinableGames()));
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
     * @author Giuseppe Laguardia
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
     * @author Giuseppe Laguardia
     * sends a SuccessMessage or ExceptionMessage to the client as reply to his command
     * @param msg the message sent
     * @throws IOException when a connection problem occurss
     */
    public void answerClient(ServerMessage msg) throws IOException {
        output.writeObject(msg);
    }
    /**
     * @author Giuseppe Laguardia
     * Sends a message to all user containing a view's update, using viewUpdater
     * @param msg the Server Message to send
     * @throws IOException when a connection problem occurs
     */
    public void broadCast (ServerMessage msg) throws IOException {
        myViewUpdater.sendAll(msg);
    }

    /**
     * @author Giuseppe Laguardia
     * getter of the game controller
     * @return the controller
     */
    public Controller getController(){
        return this.controller;
    }

    /**
     * @author Giuseppe Laguardia
     * @return all clients
     */

    public Map<UUID,ObjectOutputStream> getAllClients(){
        return myViewUpdater.getClients();
    }

    /**
     * @author Giuseppe Laguardia
     * Sends a message to a user containing a view's update, using viewUpdater
     * @param id the user's identifier
     * @param message the Server Message to send
     * @throws IOException when a connection problem occurs
     */

    public void sendTo(UUID id, ServerMessage message) throws IOException {
        myViewUpdater.sendTo(message, id);
    }

    /**
     * removes the client
     * @author Giuseppe Laguardia
     * @param myID
     */
    public void removeClient(UUID myID) {
        myViewUpdater.removeClient(myID);
    }

    /**
     * gets the view container
     * @author Giuseppe Laguardia
     * @return
     */
    public ViewRMIContainer getViewContainer() {
        return viewContainer;
    }

    /**
     * sets the controller
     * @author Giuseppe Laguardia
     * @param c
     */
    public void setController(Controller c) {
        controller=c;
    }

    /**
     * sets the view updater
     * @author Giuseppe Laguardia
     * @param gameID
     */
    public void setMyViewUpdater(UUID gameID){
        myViewUpdater=viewUpdaterMap.get(gameID);
        if(myViewUpdater==null){
            myViewUpdater=new ViewUpdater();
            addViewUpdater(gameID,myViewUpdater);
            new RMIToSocketDispatcher(controller,myViewUpdater).start();
        }
    }

    /**
     * adds the view updater
     * @author Giuseppe Laguardia
     * @param gameID
     * @param viewUpdater
     */
    public void addViewUpdater(UUID gameID,ViewUpdater viewUpdater) {
        viewUpdaterMap.put(gameID,viewUpdater);
        myViewUpdater=viewUpdater;
    }

    /**
     * gets the view updater
     * @author Giuseppe Laguardia
     *
     * @return
     */
    public ViewUpdater getViewUpdater() {
        return myViewUpdater;
    }

    /**
     * closes the connection
     * @author Giuseppe Laguardia
     */
    public void closeConnection() {
        try {
            client.close();
        } catch (IOException ignore) {}
    }
}
