package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
 * ExceptionMessage sent by the server when OnlyOneGameException occurs
 */
public class OnlyOneGameMessage extends ExceptionMessage{

    @Override
    public void processMessage(ViewSocket view) {

    }

}
