package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.UUID;

public abstract class GameState{
    protected Game game;

    protected HashMap<UUID, Player> userIDs= new HashMap<>();
    protected GameManager gameManager;

    /**
     * @author Giuseppe Laguardia
     * constructor of GameState
     * @param gameManager
     */
    GameState(GameManager gameManager){
        this.gameManager=gameManager;

    }
    public HashMap<UUID, Player> getUserIDs() {
        return userIDs;
    }

    /**
     * @author Riccardo Lapi
     * @param userId the user unique id
     * @return the Player associated to the userId
     */
    public Player getPlayerFromUid(UUID userId){
        return getUserIDs().get(userId);
    }
    /**
     * checks if it's the player's turn, if the card is legal and if the position is legal
     * @author Giorgio Mattina
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws InvalidCardException
     * @throws IllegalPositionException
     */
    protected void CheckTurnCardPosition(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException, HandNotFullException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        //checks if selectedCard is in player's hand
        if(!game.getCurrentPlayer().getHand().contains(selectedCard)){
            throw new InvalidCardException();
        }
        if(game.getCurrentPlayer().getHand().size()<3){
            throw new HandNotFullException();
        }
        //cheks if given position is in the availablePosition list
        if(!game.getCurrentPlayer().getAvailablePositions().contains(position)){
            throw new IllegalPositionException();
        }
    }
    public  void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        throw new IllegalOperationException();
    }
    protected abstract GameState nextState();
}
