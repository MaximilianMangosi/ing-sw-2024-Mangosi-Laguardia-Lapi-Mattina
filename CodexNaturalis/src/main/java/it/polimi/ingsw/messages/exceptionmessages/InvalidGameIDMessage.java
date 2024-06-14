package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.InvalidGameID;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

public class InvalidGameIDMessage extends  ExceptionMessage {
    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException, PlayerNameNotUniqueException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, InvalidUserId, InvalidGoalException, HandFullException, DeckEmptyException, InvalidChoiceException, InvalidGameID {
        throw new InvalidGameID();
    }
}
