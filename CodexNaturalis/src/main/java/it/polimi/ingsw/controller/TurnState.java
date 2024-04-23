package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public class TurnState extends GameState{
    TurnState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }
    public void playCardFront(Card selectedCard, Coordinates position, Integer userId) throws IsNotYourTurnException, RequirementsNotMetException {
        //checks if it's the player's turn
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }

        //checks if the player's hand is full
        game.playCardFront(selectedCard,position);

    }





    //TODO playCards Giorgio
    //todo drawCards Giorgio
    //isFinalTurn() return
}
