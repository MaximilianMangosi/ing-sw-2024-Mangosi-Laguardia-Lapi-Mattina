package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sends messages to the clients for views update
 * @author Giuseppe Laguardia
 */
public class ViewUpdater  {
    private final Map<UUID, ObjectOutputStream> clients = new ConcurrentHashMap<>();

    /**
     * adds client outputStream to the map
     * @author Giuseppe Laguardia
     * @param userId the user id
     * @param out the outputStream to be added
     */
    public void addClient(UUID userId, ObjectOutputStream out){
        clients.put(userId,out);

    }

    /**
     * gets  all the clients
     * @author Giuseppe Laguardia
     * @return
     */
    public Map<UUID,ObjectOutputStream> getClients(){
        return clients;
    }

    /**
     * sends  messages to all the clients
     * @author Giuseppe Laguardia
     * @param msg
     * @throws IOException
     */
    public synchronized void sendAll(ServerMessage msg) throws IOException {
        for (ObjectOutputStream c : clients.values()){
            c.writeObject(msg);
        }

    }

    /**
     * sends message to a  user
     * @author Giuseppe Laguardia
     * @param message the message
     * @param userId to of the receiver
     * @throws IOException
     */
    public synchronized void sendTo(ServerMessage message,UUID userId) throws IOException{
        clients.get(userId).writeObject(message);
    }

    /**
     * removes client from map
     * @author Giuseppe Laguardia
     * @param myID
     */
    public void removeClient(UUID myID) {
        clients.remove(myID);
    }


}
