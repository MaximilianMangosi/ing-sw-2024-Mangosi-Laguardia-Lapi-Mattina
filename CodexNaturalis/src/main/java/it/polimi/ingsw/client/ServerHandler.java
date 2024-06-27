package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.view.ViewSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Thread for handle messages send by the server on port 3232, if the user chose TUI awakes the UpdateTUI thread everytime a message arrives
 */
public class ServerHandler extends Thread{
    private ObjectInputStream input;
    private ViewSocket view;
    private final UpdateTUI updateTUI;
    private final GameKey gameKey;
    private final String serverAddress;

    /***
     * Constructor used by TUI
     * @param view the game's view
     * @param tuiUpdater the
     * @param gameKey the GameKey send to the server
     * @param serverAddress the Server's IP address
     */
    public ServerHandler(ViewSocket view, UpdateTUI tuiUpdater, GameKey gameKey,String serverAddress)  {
        this.gameKey=gameKey;
        this.view=view;
        updateTUI=tuiUpdater;
        this.serverAddress=serverAddress;
    }
    /***
     * Constructor used by GUI
     * @param view the game's view
     * @param gameKey the GameKey send to the server
     * @param serverAddress the Server's IP address
     */
    public ServerHandler(ViewSocket view,GameKey gameKey,String serverAddress)  {
        this.gameKey=gameKey;
        this.view=view;
        updateTUI=null;
        this.serverAddress=serverAddress;

    }

    @Override
    public void run() {
        try {
            Socket UVsocket= new Socket( serverAddress,3232);
            ObjectOutputStream output= new ObjectOutputStream(UVsocket.getOutputStream());
            input=new ObjectInputStream(UVsocket.getInputStream());
            output.writeObject(gameKey);
        } catch (IOException e) {
            System.out.println("UpdateViewSocket not found");
            System.exit(1);
        }
        try {
            while (true){
                ServerMessage message = (ServerMessage) input.readObject();
                message.processMessage(view);
                if(updateTUI!=null) { // null when user choose ClientGUI
                    synchronized (updateTUI) {
                        updateTUI.notifyAll();
                    }
                }
            }
        } catch (IOException  | ClassNotFoundException e ) {
            System.out.println("Connection error!");
            System.exit(1);
        }
    }
}
