package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

/**
 * Thread that until user is connected acknowledges the server that no disconnection occurred.
 *
 */
public class PingPong extends  Thread{
    View view;
    UUID myID;
    public PingPong(View view, UUID id){
       this.view=view;
       myID=id;
    }
    @Override
    public void run() {
        while (true) {
            try {
                if(!view.amIPinged(myID)) {
                    view.pong(myID);
                    Thread.sleep(30000);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection Error ping");
                System.exit(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
