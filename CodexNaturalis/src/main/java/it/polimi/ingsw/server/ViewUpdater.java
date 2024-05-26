package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ViewUpdater  {
    private final Map<UUID, ObjectOutputStream> clients = new ConcurrentHashMap<>();

    public void addClient(UUID userId, ObjectOutputStream out){
        clients.put(userId,out);

    }
    public Map<UUID,ObjectOutputStream> getClients(){
        return clients;
    }
    public synchronized void sendAll(ServerMessage msg) throws IOException {
        for (ObjectOutputStream c : clients.values()){
            c.writeObject(msg);
        }

    }
    public synchronized void sendTo(ServerMessage message,UUID userId) throws IOException{
        clients.get(userId).writeObject(message);
    }

}
