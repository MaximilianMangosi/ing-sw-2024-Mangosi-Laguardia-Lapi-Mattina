package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;

/**
 * @author Giuseppe Laguardia
 * CloseGame thread handles the case when the game is finished but some users didn't close the game. It deletes the game from GameManager opportunely communicating to the users
 */
public class CloseGame extends Thread{
    private Controller controller;
   CloseGame(Controller controller){
       this.controller=controller;
   }
    @Override
    public void run() {
       System.out.println("CloseGame thread is running");
       boolean shouldWait=true;
        while(true){
            if (controller.isGameEnded()) {
                // if the game has ended but some players didn't close the game, the thread waits for 2 minutes then deletes the game
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
