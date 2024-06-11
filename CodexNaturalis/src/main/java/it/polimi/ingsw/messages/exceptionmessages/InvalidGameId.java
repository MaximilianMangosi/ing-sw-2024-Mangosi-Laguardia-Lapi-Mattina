package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

public class InvalidGameId extends ExceptionMessage{
    @Override
    public void processMessage() throws InvalidGameID {
        try {
            super.processMessage();
        } catch (InvalidGoalException | UnacceptableNumOfPlayersException | IllegalOperationException |
                 PlayerNameNotUniqueException | IsNotYourTurnException | RequirementsNotMetException |
                 IllegalPositionException | InvalidCardException | HandNotFullException | InvalidUserId |
                 HandFullException | DeckEmptyException | InvalidChoiceException ignore) {

        }
    }
}
