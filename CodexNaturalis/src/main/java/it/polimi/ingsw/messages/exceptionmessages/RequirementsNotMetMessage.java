package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;
/**
 * ExceptionMessage send by the server in response to the Gold card requirements are not met
 */
public class RequirementsNotMetMessage extends ExceptionMessage {

    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException,  PlayerNameNotUniqueException, IsNotYourTurnException, RequirementsNotMetException {
        throw new RequirementsNotMetException();
    }
}
