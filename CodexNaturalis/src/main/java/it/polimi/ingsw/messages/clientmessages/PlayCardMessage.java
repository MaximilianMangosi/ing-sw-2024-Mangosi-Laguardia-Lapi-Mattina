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

    /**
     * constructor of PlayCardMessage
     * @author Giorgio Mattina
     * @param chosenCard the object representing the chosen card by the player
     * @param position the position in which the card must be placed
     * @param userId the id of the player who is playing
     * @param playFront true if the card is played front, false if back
     */
    PlayCardMessage(Card chosenCard,Coordinates position, UUID userId,boolean playFront){
        this.chosenCard=chosenCard;
        this.position=position;
        this.userId=userId;
        this.playFront=playFront;
    }

    /**
     * override, gets the controller from ClientHandler and either calls playCardFront or
     * playCardBack based on the value of this.playFront
     * @author Giorgio Mattina
     * @param clientHandler
     */
    @Override
    public void processMessage( ClientHandler clientHandler) {
        if(playFront){
            try {
                clientHandler.getController().playCardFront(chosenCard,position,userId);
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
                clientHandler.getController().playCardBack(chosenCard,position,userId);
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
