package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * keeps calling ping on the controller, which sets the boolean value in pingMap to false for each UUID
 */
public class DisconnectionHandler{
    private Controller controller;
    public DisconnectionHandler(Controller controller){
        this.controller=controller;
    }
    public void start() {
        try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1)) {

            Runnable task = () -> {
                try {
                    controller.checkPong();
                    controller.ping();
                } catch (RemoteException e) {
                    System.out.println("Connection error");
                    scheduler.shutdown();
                }
            };

            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
        }
    }

}
