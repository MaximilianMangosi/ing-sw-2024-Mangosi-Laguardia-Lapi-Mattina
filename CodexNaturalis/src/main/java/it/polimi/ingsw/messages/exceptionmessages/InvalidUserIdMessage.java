package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *   ExceptionMessage sent by the server when InvalidUserIdException occurs
 */
public class InvalidUserIdMessage extends ExceptionMessage{
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws  InvalidUserId {
        throw new InvalidUserId();
    }
}
