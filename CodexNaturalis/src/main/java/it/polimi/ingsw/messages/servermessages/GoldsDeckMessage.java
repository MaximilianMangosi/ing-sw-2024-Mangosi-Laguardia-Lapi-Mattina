package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends the gold deck elements
 */
public class GoldsDeckMessage extends  ServerMessage {
    int num;
    Reign top;

    public GoldsDeckMessage(int num, Reign top) {
        this.num = num;
        this.top= top;
    }

    @Override
    public void processMessage(ViewSocket view) {
        GameData gameData = view.getGameData();
        gameData.setNumOfGoldCards(num);
        gameData.setTopOfGoldsDeck(top);
    }
}
