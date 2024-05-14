package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.view.ViewSocket;

import java.io.*;

public class ServerHandler extends Thread{
    private ObjectOutputStream output;
    private final ObjectInputStream input;
    private ViewSocket view;
    private final UpdateTUI updateTUI;

    public ServerHandler(ViewSocket view, UpdateTUI tuiUpdater) throws IOException {
        this.output=view.getOutput();
        this.input=view.getInput();
        this.view=view;
        updateTUI=tuiUpdater;
    }
    public ServerHandler(ViewSocket view) throws IOException {
        this.output=view.getOutput();
        this.input=view.getInput();
        this.view=view;
        updateTUI=null;

    }

    @Override
    public void run() {
        try {
            while (true){
                ServerMessage message;
                synchronized (input) {
                    //input.wait(3000);
                    message = (ServerMessage) input.readObject();
                }
                message.processMessage(view);
                synchronized (updateTUI) {
                    updateTUI.notifyAll();
                }
            }
        } catch (IOException  | ClassNotFoundException e ) {
            System.out.println("Connection error!");
            System.exit(1);
        } //catch (InterruptedException ignore) {}
    }
}
