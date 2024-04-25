package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.HandNotFullException;
import it.polimi.ingsw.controller.exceptions.IllegalPositionException;
import it.polimi.ingsw.controller.exceptions.InvalidCardException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

import java.util.UUID;

public class FinalTurnState extends GameState{
    FinalTurnState(Game game, GameManager gameManager) {
        super(gameManager);
        this.game=game;
    }
    /**
     * checks for Turn rights, and calls playCardFront
     * @author Giorgio Mattina
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     */
    public void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException {

        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard, position, userId);
        game.playCardFront(selectedCard,position);
        //TODO check if curr player is last of the list and if so compute goalPoints for each player after that call getWinner()

    }

    @Override
    protected GameState nextState() {
        return new TerminalState(game,gameManager);
    }


    //TODO setWinner

}
