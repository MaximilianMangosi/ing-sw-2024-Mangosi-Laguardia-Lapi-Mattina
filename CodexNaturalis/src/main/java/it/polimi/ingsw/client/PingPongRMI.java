package it.polimi.ingsw.client;

import it.polimi.ingsw.view.ViewRMIInterface;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Thread that ,until user is connected to the remote object, acknowledges the server that no disconnection occurred.
 *
 */
public class PingPongRMI extends  Thread{
    ViewRMIInterface view;
    UUID myID;
    PingPongRMI(ViewRMIInterface View, UUID id){
       this.view=view;
       myID=id;
    }
    @Override
    public void run() {
        while (true) {
            try {
                if(!view.amIPinged(myID))
                    view.pong(myID);
            } catch (RemoteException e) {
                System.out.println("Connection Error");
                System.exit(1);
            }
        }
    }
}
