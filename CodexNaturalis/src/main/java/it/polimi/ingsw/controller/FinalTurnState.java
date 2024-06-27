package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.HandNotFullException;
import it.polimi.ingsw.controller.exceptions.IllegalPositionException;
import it.polimi.ingsw.controller.exceptions.InvalidCardException;
import it.polimi.ingsw.controller.exceptions.IsNotYourTurnException;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;
import java.util.UUID;

public class FinalTurnState extends GameState{
    /**
     * constructor of FinalState class
     * @author Giuseppe Laguardia
     * @param game
     * @param gameManager
     */
    FinalTurnState(Game game, GameManager gameManager, HashMap<UUID,Player> uuids) {
        super(gameManager);
        this.game=game;
        this.userIDs=uuids;
    }
    /**
     * checks for Turn rights, and calls playCardFront
     * if the player is the last one, calculate the goal points, and set it in player.goalPoints
     * @author Riccardo Lapi and Giorgio Mattina
     * @param selectedCard the card to be played
     * @param position the position on the field where place the card
     * @param userId the user ID of the user playing the card
     * @return true if it's the last player's turn
     * @throws IsNotYourTurnException when it's not user turn
     * @throws InvalidCardException when the card can't be played
     * @throws IllegalPositionException when the position is not valid
     * @throws HandNotFullException when the user already played a card this turn
     * @throws RequirementsNotMetException when the user's doesn't fulfill the card's requirements
     */
    public boolean playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException {

        //checks if it's the player's turn, if the card is legal and if the position is legal
        CheckTurnCardPosition(selectedCard, position, userId);
        game.playCardFront(selectedCard,position);
        game.nextTurn();

        return isGameOver();
    }

    /**
     * @author Riccardo Lapi and Giuseppe Laguardia
     * if is the first player calculate the goal points
     * @return true if the passed player is the first one in the order
     */
    private boolean isGameOver() {
        Player player=game.getCurrentPlayer();
        if(game.getPlayers().getFirst().equals(player)){
            for(Player p : game.getPlayers()){

                Goal privateGoal = p.getGoal();
                Goal[] publicGoals = game.getPublicGoals();

                int totGoalsPoint = privateGoal.calculateGoal(p);
                totGoalsPoint += publicGoals[0].calculateGoal(p);
                totGoalsPoint += publicGoals[1].calculateGoal(p);

                p.setGoalPoints(totGoalsPoint);
            }

            return true;
        }
        return false;
    }

    /**
     * Checks for turn rights, then calls playCardBack on the model . If it's the last player who is playing
     * it calculates the goalPoints of each player and returns true
     * @author Riccardo Lapi
     * @param selectedCard the card to be played
     * @param position the position on the field where place the card
     * @param userId the user ID of the user playing the card
     * @return true if it's the last player's turn
     * @throws IsNotYourTurnException when it's not user turn
     * @throws InvalidCardException when the card can't be played
     * @throws IllegalPositionException when the position is not valid
     * @throws HandNotFullException when the user already played a card this turn
     */
    public boolean playCardBack(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, InvalidCardException, IllegalPositionException, HandNotFullException {
        CheckTurnCardPosition(selectedCard,position,userId);
        game.playCardBack(selectedCard,position);
        game.nextTurn();
        return isGameOver();
    }

    /**
     * Override of nextState, if the last
     * @author Giuseppe Laguardia
     * @return a new TerminalState object
     */
    @Override
    protected GameState nextState() {
        if(game.getPlayers().getFirst().equals(game.getCurrentPlayer())) {
            return new TerminalState(game, gameManager, userIDs);
        }
        return this;
    }




}
