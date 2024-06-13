package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.ViewRMIContainer;

import java.rmi.RemoteException;

/**
 * @author Giuseppe Laguardia
 * CloseGame thread handles the case when the game is finished but some users didn't close the game. It deletes the game from GameManager opportunely communicating to the users
 */
public class CloseGame extends Thread{
    private final Controller controller;
   public CloseGame(Controller controller){
       this.controller=controller;
   }
    @Override
    public void run() {
       System.out.println("CloseGame thread is running");
       boolean shouldWait=true;
        try {
            while(true){
                if (controller.isGameEnded()) {
                    // if the game has ended but some players didn't close the game, the thread waits for 2 minutes then deletes the game
                    if (shouldWait) {
                        sleep(120000);
                        shouldWait = false;
                    } else {
                        controller.deleteGameFromGameManager();
                        break;
                    }
                }
            }
        } catch( RemoteException| InterruptedException e ) {
            System.out.println("game crash in close game thread");
        }
    }
}
