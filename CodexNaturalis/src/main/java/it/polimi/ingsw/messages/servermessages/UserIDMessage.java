package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

import java.util.UUID;

/**
 * Message sent by the server to notify the user that he joined/created a game ,containing his identifier
 * @author Giuseppe Laguardia
 */
public class UserIDMessage extends ServerMessage{
    UUID yourID;

    /**
     * Returns the userID contained in the message
     * @return UUID that identifies the user's receiving this message
     */
    public UUID getYourID() {
        return yourID;
    }

    /**
     * Set the userID of the client in the view
     * @param view the client's view
     */
    @Override
    public void processMessage(ViewSocket view) {
    }

    @Override
    public void processMessage() throws UnacceptableNumOfPlayersException {

    }
}
