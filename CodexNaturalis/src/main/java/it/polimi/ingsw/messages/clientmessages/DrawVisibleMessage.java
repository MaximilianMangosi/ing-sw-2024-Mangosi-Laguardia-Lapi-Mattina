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

    @Override
    public void processMessage(Controller controller, ClientHandler clientHandler) {
        try {
            controller.drawVisibleCard(userId,chosenCard);
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
