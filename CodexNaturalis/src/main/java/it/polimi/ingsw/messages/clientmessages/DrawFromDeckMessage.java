package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.DeckEmptyException;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidChoiceException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.messages.exceptionmessages.*;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.UUID;

public class DrawFromDeckMessage extends ClientMessage{
    private Integer chosenDeck;
    private UUID userId;

    /**
     * constructor of DrawFromDeckMessage
     * @author Giorgio Mattina
     * @param chosenDeck either the resource deck (0) or gold (!=0)
     * @param userId the UUID of the player that called drawFromDeck
     */
    public DrawFromDeckMessage(Integer chosenDeck, UUID userId){
        this.chosenDeck=chosenDeck;
        this.userId=userId;
    }

    /**
     * Override, gets the controller from the ClientHandler and calls drawFromDeck on the controller
     * @author Giorgio Mattina
     * @param clientHandler the object that called processMessage
     */
    @Override
    public void processMessage( ClientHandler clientHandler) throws IOException {
        try {
            Controller c = clientHandler.getController();
            c.drawFromDeck(userId,chosenDeck);
            SuccessMessage answer = new SuccessMessage();
            HandMessage handMessage = new HandMessage(c.getPlayersHands().get(userId));

            clientHandler.answerClient(answer);
            clientHandler.sendTo(userId,handMessage);
            clientHandler.broadCast(new TurnMessage(c.getCurrentPlayer()));
            if(chosenDeck==0)
                clientHandler.broadCast(new NumOfResourceCardsMessage(c.getNumOfResourceCards()));
            else clientHandler.broadCast(new NumOfGoldCardsMessage(c.getNumOfGoldCards()));
        } catch (IsNotYourTurnException e) {
            IsNotYourTurnMessage answer =new IsNotYourTurnMessage();
            clientHandler.answerClient(answer);

        } catch (HandFullException e) {
            HandFullMessage answer = new HandFullMessage();
            clientHandler.answerClient(answer);

        } catch (DeckEmptyException e) {
            DeckEmptyMessage answer= new DeckEmptyMessage();
            clientHandler.answerClient(answer);

        } catch (IllegalOperationException e) {
            IllegalOperationMessage answer = new IllegalOperationMessage(e);
            clientHandler.answerClient(answer);
        } catch (InvalidChoiceException e) {
            InvalidChoiceMessage answer =new InvalidChoiceMessage();
            clientHandler.answerClient(answer);
        }
    }
}
