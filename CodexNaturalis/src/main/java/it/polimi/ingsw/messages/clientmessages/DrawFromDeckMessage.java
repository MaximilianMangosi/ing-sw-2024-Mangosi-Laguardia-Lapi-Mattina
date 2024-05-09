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


    @Override
    public void processMessage(Controller controller, ClientHandler clientHandler) {
        try {
            controller.drawFromDeck(userId,chosenDeck);
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
