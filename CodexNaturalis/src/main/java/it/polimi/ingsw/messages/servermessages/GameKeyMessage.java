package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.controller.GameKey;
import it.polimi.ingsw.view.ViewSocket;
/**
 * @author Giuseppe Laguardia
 * sends the game key
 */
public class GameKeyMessage extends ServerMessage{
    private GameKey gameKey;

    public GameKeyMessage(GameKey gameKey) {
        this.gameKey = gameKey;
    }

    public GameKey getGameKey() {
        return gameKey;
    }

}
