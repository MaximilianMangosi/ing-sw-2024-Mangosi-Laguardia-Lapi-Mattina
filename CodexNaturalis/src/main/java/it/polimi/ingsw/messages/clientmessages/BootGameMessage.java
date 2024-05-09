package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.server.ClientHandler;

public class BootGameMessage extends ClientMessage{
    private int numPlayers;
    private String username;


    public void processMessage(Controller controller, ClientHandler clientHandler)  {
        try {
            controller.bootGame(numPlayers,username);
        } catch (UnacceptableNumOfPlayersException e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (OnlyOneGameException e) {
            throw new RuntimeException(e);
        }
    }

}
