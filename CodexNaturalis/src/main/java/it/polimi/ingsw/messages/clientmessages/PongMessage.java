package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.UUID;

public class PongMessage extends ClientMessage {
    UUID myID;
    public PongMessage(UUID myID) {
     this.myID=myID;
    }
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        clientHandler.getController().pong(myID);
        clientHandler.answerClient( new SuccessMessage());
    }
}
