package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
 * ExceptionMessage send by the server in response to BootGameMessage if UnacceptableNumOfPlayersException occurs
 */
public class UnacceptableNumOfPlayersMessage extends ExceptionMessage{
    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException {
        throw new UnacceptableNumOfPlayersException();
    }

    @Override
    public void processMessage(ViewSocket view) {
    }
}
