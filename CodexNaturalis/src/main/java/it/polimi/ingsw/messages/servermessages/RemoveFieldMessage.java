package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.view.ViewSocket;

/**
 * Message sent by the server when a users closed the game or has been kicked and remove his field from clients View
 */
public class RemoveFieldMessage extends ServerMessage {
    String player;

    public RemoveFieldMessage(String player) {
        this.player = player;
    }

    @Override
    public void processMessage(ViewSocket view) {
        view.getGameData().removePlayerField(player);    
    }
}
