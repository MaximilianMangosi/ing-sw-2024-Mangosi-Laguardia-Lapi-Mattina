package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *   ExceptionMessage sent by the server when DeckEmptyException occurs
 */
public class DeckEmptyMessage extends  ExceptionMessage{
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws DeckEmptyException {
        throw new DeckEmptyException();
    }
}
