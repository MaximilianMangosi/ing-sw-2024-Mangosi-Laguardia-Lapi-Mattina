package it.polimi.ingsw.messages.exceptionmessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.servermessages.ServerMessage;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;
import it.polimi.ingsw.view.ViewSocket;

public abstract class ExceptionMessage extends ServerMessage {

    public abstract void processMessage() throws UnacceptableNumOfPlayersException;
}
