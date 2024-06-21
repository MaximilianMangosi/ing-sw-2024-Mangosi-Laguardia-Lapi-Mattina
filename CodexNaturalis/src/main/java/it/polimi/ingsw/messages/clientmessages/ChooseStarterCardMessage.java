package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.InvalidUserIdMessage;
import it.polimi.ingsw.messages.servermessages.FieldMessage;
import it.polimi.ingsw.messages.servermessages.LegalPositionMessage;
import it.polimi.ingsw.messages.servermessages.StarterCardMessage;
import it.polimi.ingsw.messages.servermessages.SuccessMessage;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.UUID;

public class ChooseStarterCardMessage extends ClientMessage{
    private boolean isFront;
    private UUID userId;

    /**
     * constructor of ChooseStarterCardMessage
     * @param isFront
     * @param userId
     */
    public ChooseStarterCardMessage(boolean isFront, UUID userId){
        this.isFront=isFront;
        this.userId=userId;
    }

    /**
     * Overriding, gets the controller from the ClientHandler and calls
     * chooseStarterCardSide on the controller
     * @param clientHandler, the object that called processMessage
     */
    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException{
        try {
            Controller c = clientHandler.getController();
            String playerName = c.getPlayer(userId).getName();
            c.chooseStarterCardSide(isFront,userId);
            clientHandler.answerClient(new SuccessMessage());
            clientHandler.sendTo(userId,new StarterCardMessage(c.getPlayersStarterCards().get(userId)));
            clientHandler.broadCast(new FieldMessage(c.getPlayersField().get(playerName),c.getView().getFieldBuildingHelper(playerName),playerName));
            clientHandler.sendTo(userId,new LegalPositionMessage(c.getPlayersLegalPositions().get(userId)));
        } catch (InvalidUserId e) {
            clientHandler.closeConnection();
        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        }
    }
}
