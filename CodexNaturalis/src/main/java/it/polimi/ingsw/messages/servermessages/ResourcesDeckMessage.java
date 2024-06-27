package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.client.GameData;
import it.polimi.ingsw.model.gamecards.resources.Reign;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 *sends the updated resource deck
 */
public class ResourcesDeckMessage extends ServerMessage{
    int num;
    Reign top;

    public ResourcesDeckMessage(int num, Reign top) {
        this.num = num;
        this.top=top;
    }

    @Override
    public void processMessage(ViewSocket view) {
        GameData gameData = view.getGameData();
        gameData.setNumOfResourceCards(num);
        gameData.setTopOfResourcesDeck(top);


    }
}
