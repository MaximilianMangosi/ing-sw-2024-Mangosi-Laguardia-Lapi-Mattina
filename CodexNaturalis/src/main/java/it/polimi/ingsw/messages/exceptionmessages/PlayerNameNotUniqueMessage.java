package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
 * @author Giuseppe Laguardia
 * ExceptionMessage sent by the server when PlayerNameNotUniqueException occurs
 */
public class PlayerNameNotUniqueMessage extends ExceptionMessage{
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException,  PlayerNameNotUniqueException {
        throw new PlayerNameNotUniqueException();
    }
}
