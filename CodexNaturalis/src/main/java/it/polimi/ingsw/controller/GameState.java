package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
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
     * @param game
     * @param gameManager
     */
    GameState(Game game,GameManager gameManager){
        this.game=game;
        this.gameManager=gameManager;

    }
    public HashMap<UUID, Player> getUserIDs() {
        return userIDs;
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
    protected void CheckTurnCardPosition(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException {
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        //checks if selectedCard is in player's hand
        if(!game.getCurrentPlayer().getHand().contains(selectedCard)){
            throw new InvalidCardException();
        }
        //cheks if given position is in the availablePosition list
        if(!game.getCurrentPlayer().getAvailablePositions().contains(position)){
            throw new IllegalPositionException();
        }
    }
}
