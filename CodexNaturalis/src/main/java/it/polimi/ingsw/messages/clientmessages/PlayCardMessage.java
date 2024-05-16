package it.polimi.ingsw.messages.clientmessages;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.exceptionmessages.*;
import it.polimi.ingsw.messages.servermessages.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
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
    public PlayCardMessage(Card chosenCard, Coordinates position, UUID userId, boolean playFront){
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
    public void processMessage( ClientHandler clientHandler) throws IOException {
        Controller c = clientHandler.getController();
        try {
            if (playFront) {

                c.playCardFront(chosenCard, position, userId);
            } else {
                clientHandler.getController().playCardBack(chosenCard, position, userId);
            }
            if (c.getView().getWinner()!=null){
                clientHandler.broadCast(new GameEndMessage(c.getView().getWinner()));
            }
            String playerName = c.getUserIDs().get(userId).getName();
            clientHandler.sendTo(userId,new HandMessage(c.getPlayersHands().get(userId)));
            clientHandler.broadCast(new FieldMessage( c.getPlayersField().get(playerName),c.getView().getFieldBuildingHelper(playerName),playerName));
            clientHandler.broadCast(new PointsMessage( c.getPlayersPoints()));
            clientHandler.broadCast(new TurnMessage(c.getCurrentPlayer()));
            clientHandler.sendTo(userId,new LegalPositionMessage(c.getPlayersLegalPositions().get(userId)));

        } catch (HandNotFullException e) {
            clientHandler.answerClient(new HandNotFullMessage());
        } catch (IsNotYourTurnException e) {
            clientHandler.answerClient(new IsNotYourTurnMessage());
        } catch (RequirementsNotMetException e) {
           clientHandler.answerClient(new RequirementsNotMetMessage());
        } catch (IllegalPositionException e) {
            clientHandler.answerClient(new IllegalPositionMessage());
        } catch (IllegalOperationException e) {
            clientHandler.answerClient( new IllegalOperationMessage(e));
        } catch (InvalidCardException e) {
            clientHandler.answerClient(new InvalidCardMessage());
        } catch (RemoteException ignore) {
        }

    }
}
