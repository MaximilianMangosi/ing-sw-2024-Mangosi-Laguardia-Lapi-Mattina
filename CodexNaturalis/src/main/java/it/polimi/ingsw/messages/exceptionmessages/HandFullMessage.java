package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *  ExceptionMessage sent by the server when HandFullException occurs
 */
public class HandFullMessage extends ExceptionMessage{
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws HandFullException {
        throw new HandFullException();
    }
}
