package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.UUID;

public class CloseGameMessage extends ClientMessage{
    UUID myID;

    /**
     * constructor of CLoseGameMessage
     * @author Giuseppe Laguardia
     * @param myID of the player who wants to close the game
     */
    public CloseGameMessage(UUID myID) {
        this.myID = myID;
    }
    /**
     * calls the controller and answers the client with SuccessMessage,PlayersListMessage,RemoveFieldMessage,TurnMessage,PointsMessage
     * @author Giuseppe Laguardia
     * @param clientHandler the father thread that can send a response to the client
     * @throws IOException
     */
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
