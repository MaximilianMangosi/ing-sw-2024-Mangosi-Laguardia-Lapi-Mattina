package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;

public class CloseGameRMI extends Thread{
    Controller controller;
   CloseGameRMI(Controller controller){
       this.controller=controller;
   }
    @Override
    public void run() {
       boolean wait=false;
        while(true){
            if(controller.getGame()!=null) {  //
                if (controller.getUserIDs().size()<2) {
                    try {
                        controller.deleteGameFromGameManager();
                    } catch (RemoteException e) {
                        break;
                    }
                }
                if (controller.isGameEnded()) {
                    // if the game is ended but some players didn't close the game, the thread waits for 2 minutes then deletes the game
                    if (wait) {
                        try {
                            controller.deleteGameFromGameManager();
                        } catch (RemoteException e) {
                            System.out.println("dis");
                            break;
                        }
                    }
                    wait = true;
                    try {
                        sleep(120000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
    }
}
