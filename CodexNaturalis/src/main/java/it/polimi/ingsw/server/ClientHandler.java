package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.clientmessages.ClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler  implements Runnable{
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Controller controller;

    ClientHandler (Socket c,Controller controller){
        this.client=c;
        this.controller=controller;
    }

    @Override
    public void run(){
        try{
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        }catch (IOException e){
            System.out.println("Couldn't connect to "+client.getInetAddress());
        }
        System.out.println("Connected to "+client.getInetAddress());

        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("client" + client.getInetAddress() + "connection dropped\n");
        }

        try{
            client.close();
        } catch (IOException ignored) {}

    }

    private void handleConnection() throws IOException{
        try{
            while(true){
                Object next = input.readObject();
                ClientMessage cmd =(ClientMessage) next;
                cmd.processMessage(this);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid stream from client\n");
        }
    }
    public Controller getController(){
        return this.controller;
    }

}
