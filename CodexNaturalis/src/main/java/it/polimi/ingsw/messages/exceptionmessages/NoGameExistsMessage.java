package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
 * ExceptionMessage sent by the server when there is no game created
 */
public class NoGameExistsMessage extends ExceptionMessage{

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, OnlyOneGameException, IllegalOperationException, NoGameExistsException {
        throw  new NoGameExistsException();
    }
}
