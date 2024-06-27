package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;

/**
 * keeps calling ping on the controller, which sets the boolean value in pingMap to false for each UUID
 * @author Giuseppe Laguardia
 */
public class DisconnectionHandler extends Thread{
    private Controller controller;
    public DisconnectionHandler(Controller controller){
        this.controller=controller;
    }
    @Override
    public void run() {
        while (true){
            controller.ping();
            try {
                sleep(30000);
                System.out.println("Disconnection handler wakes up");
            } catch (InterruptedException ignored){}
            try {
                controller.checkPong();
            } catch (RemoteException e) {
                System.out.println("Connection error");
            }

        }

    }

}
