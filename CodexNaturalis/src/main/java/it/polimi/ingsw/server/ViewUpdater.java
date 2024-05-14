package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ViewUpdater  {
    private Map<UUID,ClientHandler> clients = new HashMap<>();

    public void addClient(UUID userId,ClientHandler c){
        this.clients.put(userId,c);
    }
    public Map<UUID,ClientHandler> getClients (){
        return  clients;
    }
    public void send(ServerMessage msg) throws IOException {
        for (ClientHandler c : clients.values()){
            c.answerClient(msg);
        }
    }
    public void sendTo(ServerMessage message,UUID userId) throws IOException{
        clients.get(userId).answerClient(message);
    }

}
