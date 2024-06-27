package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *   ExceptionMessage sent by the server when InvalidChoiceException occurs
 */
public class InvalidChoiceMessage extends ExceptionMessage{

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws InvalidChoiceException {
        throw new InvalidChoiceException();
    }
}
