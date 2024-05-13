package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.view.ViewSocket;

import java.util.List;

public class VisibleCardMessage extends ServerMessage{
    List<Card> visibleCards;
    public VisibleCardMessage(List<Card> listOfCard){
        this.visibleCards=listOfCard;
    }
    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setVisibleCards(visibleCards);
    }
}
