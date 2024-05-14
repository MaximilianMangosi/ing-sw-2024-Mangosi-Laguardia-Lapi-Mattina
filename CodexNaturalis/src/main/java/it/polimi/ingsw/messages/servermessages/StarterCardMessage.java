package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

public class StarterCardMessage extends ServerMessage{
    private StarterCard starterCard;
    public StarterCardMessage(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException, OnlyOneGameException, IllegalOperationException, NoGameExistsException, PlayerNameNotUniqueException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, InvalidUserId, InvalidGoalException, HandFullException, DeckEmptyException, InvalidChoiceException {

    }
}
