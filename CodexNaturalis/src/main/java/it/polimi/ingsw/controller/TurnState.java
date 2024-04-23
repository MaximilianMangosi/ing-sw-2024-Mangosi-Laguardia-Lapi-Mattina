package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public class TurnState extends GameState{
    TurnState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }
    /*
    public void playCardFront(ResourceCard selectedCard, Coordinates position, Integer userId){
        //checks if it's the player's turn
        if(!userIDs.get(userID).equals(game.getCurrentPlayer()){
            throw new IsNotYourTurnException();
        }

    }
    public void playCardFront(GoldCard selectedCard, Coordinates position, Integer userId){

    }
    public void playCardFront(GoldCardAngles selectedCard, Coordinates position, Integer userId){

    }
    public void playCardFront(GoldCardTool selectedCard, Coordinates position, Integer userId){

    }
    public void playCardFront(StarterCard selectedCard, Coordinates position, Integer userId){

    }
*/
    //TODO playCards Giorgio
    //todo drawCards Giorgio
    //isFinalTurn() return
}
