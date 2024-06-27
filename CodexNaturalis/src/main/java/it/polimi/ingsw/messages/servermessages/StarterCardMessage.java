package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends the starter card
 */
public class StarterCardMessage extends ServerMessage{
    private StarterCard starterCard;
    public StarterCardMessage(StarterCard starterCard) {
        this.starterCard = new StarterCard(starterCard);
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().setStarterCard(starterCard);
    }
}
