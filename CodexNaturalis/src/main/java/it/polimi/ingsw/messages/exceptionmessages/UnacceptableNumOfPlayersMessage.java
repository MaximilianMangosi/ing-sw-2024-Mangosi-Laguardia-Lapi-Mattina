package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

/**
 * Message send by the server in response to BootGameMessage if UnacceptableNumOfPlayersException occurs
 */
public class UnacceptableNumOfPlayersMessage extends ExceptionMessage{
    UnacceptableNumOfPlayersException exception;
    UnacceptableNumOfPlayersMessage(UnacceptableNumOfPlayersException e) {
        exception=e;
    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException {
        throw exception;
    }

    @Override
    public void processMessage(ViewSocket view) {
    }
}
