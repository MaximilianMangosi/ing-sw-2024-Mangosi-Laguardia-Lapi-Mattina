package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.DeckEmptyException;
import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.controller.exceptions.InvalidChoiceException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.gamecards.exceptions.HandFullException;
import it.polimi.ingsw.server.ClientHandler;

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
    public void processMessage( ClientHandler clientHandler) {
        try {
            clientHandler.getController().drawFromDeck(userId,chosenDeck);
        } catch (IsNotYourTurnException e) {
            throw new RuntimeException(e);
        } catch (HandFullException e) {
            throw new RuntimeException(e);
        } catch (DeckEmptyException e) {
            throw new RuntimeException(e);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        }
    }
}
