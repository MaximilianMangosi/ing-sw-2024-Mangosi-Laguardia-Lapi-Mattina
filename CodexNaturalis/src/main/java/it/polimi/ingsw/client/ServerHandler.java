package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.view.ViewSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class ServerHandler extends Thread{
    private ObjectInputStream input;
    private ViewSocket view;
    private final UpdateTUI updateTUI;
    private final UUID userID;

    public ServerHandler(ViewSocket view, UpdateTUI tuiUpdater, UUID userID) throws IOException {
        this.userID=userID;
        this.view=view;
        updateTUI=tuiUpdater;
    }
    public ServerHandler(ViewSocket view,UUID userID) throws IOException {
        this.userID=userID;
        this.view=view;
        updateTUI=null;

    }

    @Override
    public void run() {
        try {
            Socket UVsocket= new Socket( InetAddress.getLocalHost(),3232);
            ObjectOutputStream output= new ObjectOutputStream(UVsocket.getOutputStream());
            input=new ObjectInputStream(UVsocket.getInputStream());
            output.writeObject(userID);
        } catch (IOException e) {
            System.out.println("UpdateViewSocket not found");
            System.exit(1);
        }
        try {
            while (true){
                ServerMessage message;
                message = (ServerMessage) input.readObject();
                message.processMessage(view);
                synchronized (updateTUI) {
                    updateTUI.notifyAll();
                }
            }
        } catch (IOException  | ClassNotFoundException e ) {
            System.out.println("Connection error!");
            System.exit(1);
        }
    }
}
