package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidChoiceException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.messages.exceptionmessages.HandFullMessage;
import it.polimi.ingsw.messages.exceptionmessages.IllegalOperationMessage;
import it.polimi.ingsw.messages.exceptionmessages.InvalidCardMessage;
import it.polimi.ingsw.messages.exceptionmessages.IsNotYourTurnMessage;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
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
    public void processMessage( ClientHandler clientHandler) throws IOException {
        try {
            Controller c=clientHandler.getController();
            c.drawVisibleCard(userId,chosenCard);
            SuccessMessage ok = new SuccessMessage();
            clientHandler.answerClient(ok);
            HandMessage handMessage = new HandMessage(c.getPlayersHands().get(userId));
            clientHandler.broadCast(new TurnMessage(c.getCurrentPlayer()));
            clientHandler.broadCast(new VisibleCardMessage(c.getVisibleCards()));
            clientHandler.broadCast(new NumOfGoldCardsMessage(c.getNumOfGoldCards()));
            clientHandler.broadCast(new NumOfResourceCardsMessage(c.getNumOfResourceCards()));
        } catch (IsNotYourTurnException e) {
            clientHandler.answerClient(new IsNotYourTurnMessage());
        } catch (HandFullException e) {
            clientHandler.answerClient(new HandFullMessage());
        } catch (IllegalOperationException e) {
            clientHandler.answerClient(new IllegalOperationMessage(e));
        } catch (InvalidChoiceException e) {
            clientHandler.answerClient(new InvalidCardMessage());
        }
    }
}
