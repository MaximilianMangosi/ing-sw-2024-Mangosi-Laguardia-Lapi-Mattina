package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ViewUpdater  {
    private final Map<UUID, ObjectOutputStream> clients = new HashMap<>();

    public void addClient(UUID userId, ObjectOutputStream out){
        this.clients.put(userId,out);
    }
    public Map<UUID,ObjectOutputStream> getClients(){
        return  clients;
    }
    public boolean sendAll(ServerMessage msg) throws IOException {
        //System.out.println("check");
        if(clients.isEmpty()) {
            //System.out.println("empty");
            return true;
        }
        System.out.println("ready to send");
        for (ObjectOutputStream c : clients.values()){
            c.writeObject(msg);
        }
        System.out.println("update sent");
        return false;
    }
    public void sendTo(ServerMessage message,UUID userId) throws IOException{
        clients.get(userId).writeObject(message);
    }

}
