package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;
/**
 *  ExceptionMessage sent by the server when InvalidGoalException occurs
 */
public class InvalidGoalMessage extends ExceptionMessage{
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws InvalidGoalException {
        throw new InvalidGoalException();
    }
}
