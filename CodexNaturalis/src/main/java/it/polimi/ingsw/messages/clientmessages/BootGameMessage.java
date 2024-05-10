package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.ClientHandler;

public class BootGameMessage extends ClientMessage{
    private int numPlayers;
    private String username;

    /**
     * Constructor of BootGameMessage
     * @author Giuseppe Laguardia
     * @param numOfPlayers, the number of players that will be playing in the newly booted game
     * @param playerName the name of the player that called bootGame
     */
    public BootGameMessage(int numOfPlayers, String playerName) {
       this.numPlayers=numOfPlayers;
       this.username=playerName;
    }

    /**
     * Override, gets the controller from the clientHandler and calls bootGame on
     * the controller
     * @author Giorgio Mattina
     * @param clientHandler the object that called processMessage
     */
    public void processMessage( ClientHandler clientHandler)  {
        try {
            clientHandler.getController().bootGame(numPlayers,username);
        } catch (UnacceptableNumOfPlayersException e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (OnlyOneGameException e) {
            throw new RuntimeException(e);
        }
    }

}
