package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.view.ViewSocket;

/**
 * @author Giuseppe Laguardia
 * ExceptionMessage sent by the server when there is no game created
 */
public class NoGameExistsMessage extends ExceptionMessage{

    @Override
    public void processMessage(ViewSocket view) {

    }
}
