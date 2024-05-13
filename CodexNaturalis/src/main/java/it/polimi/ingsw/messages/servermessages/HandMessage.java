package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

import java.util.List;

public class HandMessage extends ServerMessage{
    List<Card> newHand;

    public HandMessage(List<Card> newHand) {
        this.newHand = newHand;
    }

    @Override
    public void processMessage(ViewSocket view) {
        GameData gd= view.getGameData();
        gd.setHand(newHand);
    }

}
