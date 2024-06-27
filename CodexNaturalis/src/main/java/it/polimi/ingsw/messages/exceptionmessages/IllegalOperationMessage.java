package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *   ExceptionMessage sent by the server when IllegalOperationException occurs
 */
public class IllegalOperationMessage  extends ExceptionMessage{
    IllegalOperationException exception;

    public IllegalOperationMessage(IllegalOperationException exception) {
        this.exception = exception;
    }

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException {
        throw exception;
    }
}
