package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;

public abstract class ClientMessage extends Message {

    public abstract void processMessage(ClientHandler clientHandler) throws IOException;
}
