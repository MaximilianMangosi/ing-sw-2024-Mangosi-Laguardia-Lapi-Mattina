package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.servermessages.UserIDMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Thread that keeps listening to port 3232 for new UUIDs, then adds them to ViewUpdater object
 */
public class UpdateViewSocket extends Thread{
    ServerSocket  UVSocket;
    ViewUpdater viewUpdater;

    public UpdateViewSocket(ViewUpdater viewUpdater) {
        this.viewUpdater = viewUpdater;
    }

    @Override
    public void run() {
        try {
            UVSocket= new ServerSocket(3232);
        } catch (IOException e) {
            System.out.println("socket open failed");
        }
        while (true){
            try {
                Socket client= UVSocket.accept();
                ObjectInputStream input= new ObjectInputStream(client.getInputStream());
                ObjectOutputStream output= new ObjectOutputStream(client.getOutputStream());
                UUID userID= (UUID) input.readObject();
                viewUpdater.addClient(userID,output);
                System.out.println("Client added");

            } catch (IOException e) {
                System.out.println("UpdateView socket connection dropped");
            } catch (ClassNotFoundException e) {
                System.out.println("Error reading userID of client");
            }


        }

    }
}
