package it.polimi.ingsw.server;

import java.net.Socket;

public class ClientHandler  implements Runnable{
    private Socket client;

    ClientHandler (Socket c){
        this.client=c;
    }

    @Override
    public void run(){

    }
}
