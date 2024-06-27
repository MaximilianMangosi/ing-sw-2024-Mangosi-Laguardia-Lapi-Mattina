package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalPositionException;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * ExceptionMessage sent by the server when IllegalPositionException occurs
 */
public class IllegalPositionMessage extends ExceptionMessage {

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws IllegalPositionException {
        throw  new IllegalPositionException();
    }
}
