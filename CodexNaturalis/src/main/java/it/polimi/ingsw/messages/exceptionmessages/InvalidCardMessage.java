package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.InvalidCardException;
import it.polimi.ingsw.view.ViewSocket;

public class InvalidCardMessage extends  ExceptionMessage{
/**
 * @author Giuseppe Laguardia
 *   ExceptionMessage sent by the server when InvalidCardException occurs
 */
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws InvalidCardException {
        throw new InvalidCardException();
    }
}
