package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.UUID;

public class CloseGameMessage extends ClientMessage{
    UUID myID;

    public CloseGameMessage(UUID myID) {
        this.myID = myID;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        Controller c=clientHandler.getController();
        String username= c.getUserIDs().get(myID).getName();
        c.closeGame(myID);
        clientHandler.answerClient(new SuccessMessage());
        clientHandler.broadCast(new PlayersListMessage(c.getPlayersList()));
        clientHandler.broadCast(new RemoveFieldMessage(username));
        clientHandler.broadCast(new TurnMessage(c.getCurrentPlayer()));
        clientHandler.broadCast(new PointsMessage(c.getPlayersPoints()));
    }
}
