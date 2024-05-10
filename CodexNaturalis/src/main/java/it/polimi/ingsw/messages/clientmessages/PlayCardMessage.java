package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.server.ClientHandler;

import java.util.UUID;

public class PlayCardMessage extends ClientMessage{

    private Card chosenCard;
    private Coordinates position;
    private UUID userId;
    private boolean playFront;
    @Override
    public void processMessage(Controller controller, ClientHandler clientHandler) {
        if(playFront){
            try {
                controller.playCardFront(chosenCard,position,userId);
            } catch (IsNotYourTurnException e) {
                throw new RuntimeException(e);
            } catch (RequirementsNotMetException e) {
                throw new RuntimeException(e);
            } catch (IllegalPositionException e) {
                throw new RuntimeException(e);
            } catch (InvalidCardException e) {
                throw new RuntimeException(e);
            } catch (HandNotFullException e) {
                throw new RuntimeException(e);
            } catch (IllegalOperationException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                controller.playCardBack(chosenCard,position,userId);
            } catch (HandNotFullException e) {
                throw new RuntimeException(e);
            } catch (IsNotYourTurnException e) {
                throw new RuntimeException(e);
            } catch (RequirementsNotMetException e) {
                throw new RuntimeException(e);
            } catch (IllegalPositionException e) {
                throw new RuntimeException(e);
            } catch (IllegalOperationException e) {
                throw new RuntimeException(e);
            } catch (InvalidCardException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
