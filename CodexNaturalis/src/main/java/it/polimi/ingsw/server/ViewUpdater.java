package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.ServerMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ViewUpdater  {
    private List<ClientHandler> clients = new ArrayList<>();
    public void updateView(){

    }
    public void addClient(ClientHandler c){
        this.clients.add(c);
    }
    public List<ClientHandler> getClients (){
        return  clients;
    }
    public void send(ServerMessage msg) throws IOException {
        for (ClientHandler c : clients){
            c.answerClient(msg);
        }
    }
}
