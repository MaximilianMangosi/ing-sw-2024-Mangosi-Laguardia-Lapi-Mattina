package it.polimi.ingsw.messages.servermessages;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

public abstract class ServerMessage extends Message {
    public abstract void processMessage(ViewSocket view) ;
    public abstract void processMessage() throws UnacceptableNumOfPlayersException;
}
