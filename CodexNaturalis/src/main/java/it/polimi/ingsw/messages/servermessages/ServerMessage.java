package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.*;
import it.polimi.ingsw.view.ViewSocket;

public abstract class ServerMessage extends Message {
    public void processMessage(ViewSocket view) {}
    public void processMessage() throws UnacceptableNumOfPlayersException, IllegalOperationException,  PlayerNameNotUniqueException, IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, InvalidUserId, InvalidGoalException, HandFullException, DeckEmptyException, InvalidChoiceException, InvalidGameID {}
}
