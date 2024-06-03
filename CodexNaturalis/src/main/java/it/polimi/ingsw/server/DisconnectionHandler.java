package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;

/**
 * keeps calling ping on the controller, which sets the boolean value in pingMap to false for each UUID
 */
public class DisconnectionHandler extends Thread{
    private Controller controller;
    DisconnectionHandler(Controller controller){
        this.controller=controller;
    }
    @Override
    public void run() {
        while (true){
            controller.ping();
            try {
                sleep(60000);
            } catch (InterruptedException ignored){}
            try {
                controller.checkPong();
            } catch (RemoteException e) {
                System.out.println("Connection error");
            }

        }

    }

}
