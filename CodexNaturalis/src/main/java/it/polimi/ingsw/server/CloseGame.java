package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;

public class CloseGame extends Thread{
    Controller controller;
   CloseGame(Controller controller){
       this.controller=controller;
   }
    @Override
    public void run() {
       boolean shouldWait=true;
        while(true){
            if (controller.isGameEnded()) {
                // if the game is ended but some players didn't close the game, the thread waits for 2 minutes then deletes the game
                if (shouldWait) {
                    try {
                        wait(120000);
                        shouldWait=false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        controller.deleteGameFromGameManager();
                        shouldWait = true;
                    } catch (RemoteException e) {
                        break;
                    }
                }

            }
        }
    }
}
