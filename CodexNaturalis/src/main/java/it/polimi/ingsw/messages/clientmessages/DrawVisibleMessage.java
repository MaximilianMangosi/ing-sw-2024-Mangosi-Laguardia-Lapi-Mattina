package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidChoiceException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.server.ClientHandler;

import java.util.UUID;

public class DrawVisibleMessage extends ClientMessage{
    private int chosenCard;
    private UUID userId;

    /**
     * constructor of DrawVisibleMessage
     * @author Giorgio Mattina
     * @param chosenCard
     * @param userId
     */
    public DrawVisibleMessage(int chosenCard, UUID userId){
        this.chosenCard=chosenCard;
        this.userId=userId;
    }

    /**
     * Override, gets the controller from the ClientHandler and calls drawVisibleCard on the controller
     * @param clientHandler
     */
    @Override
    public void processMessage( ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawVisibleCard(userId,chosenCard);
        } catch (IsNotYourTurnException e) {
            throw new RuntimeException(e);
        } catch (HandFullException e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        }
    }
}
