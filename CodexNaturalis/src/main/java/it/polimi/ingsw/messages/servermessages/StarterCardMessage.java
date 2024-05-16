package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

public class StarterCardMessage extends ServerMessage{
    private StarterCard starterCard;
    public StarterCardMessage(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setStarterCard(starterCard);
    }
}
