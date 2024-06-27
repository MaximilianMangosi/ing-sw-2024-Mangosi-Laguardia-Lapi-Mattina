package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.view.ViewSocket;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Giuseppe Laguardia
 * sends updated hand
 */
public class HandMessage extends ServerMessage{
    List<Card> newHand;

    public HandMessage(List<Card> newHand) {
        this.newHand = new ArrayList<>(newHand);
    }

    @Override
    public void processMessage(ViewSocket view) {
        GameData gd= view.getGameData();
        gd.setHand(newHand);
    }

}
