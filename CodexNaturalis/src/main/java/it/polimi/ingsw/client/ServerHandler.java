package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.view.ViewSocket;

import java.io.*;

public class ServerHandler extends Thread{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ViewSocket view;

    public ServerHandler( ViewSocket view) throws IOException {
        this.output=view.getOutput();
        this.input=view.getInput();
        this.view=view;
    }

    @Override
    public void run() {
        try {
            while (true){
                synchronized (input) {
                    ServerMessage message= (ServerMessage) input.readObject();
                    message.processMessage(view);
                }
            }
        } catch (IOException  | ClassNotFoundException e ) {
            System.out.println("Connection error!");
            System.exit(1);
        }
    }
}
