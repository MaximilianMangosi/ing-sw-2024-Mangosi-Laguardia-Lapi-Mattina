package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.IllegalPositionException;
import it.polimi.ingsw.controller.exceptions.InvalidCardException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

public class InvalidCardMessage extends  ExceptionMessage{
/**
 *   ExceptionMessage sent by the server when InvalidCardException occurs
 */
    @Override
    public void processMessage(ViewSocket view) {

    }

    @Override
    public void processMessage() throws InvalidCardException {
        throw new InvalidCardException();
    }
}
