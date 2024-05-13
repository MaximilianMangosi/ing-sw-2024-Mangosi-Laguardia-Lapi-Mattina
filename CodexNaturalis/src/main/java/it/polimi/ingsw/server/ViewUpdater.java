package it.polimi.ingsw.server;

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
}
