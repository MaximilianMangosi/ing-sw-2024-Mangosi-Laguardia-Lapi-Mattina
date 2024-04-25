package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.InvalidGoalException;
import it.polimi.ingsw.controller.exceptions.InvalidUserId;
import it.polimi.ingsw.model.gamecards.cards.StarterCard;
import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.*;

import java.util.Arrays;
import java.util.UUID;

/**
 * The InitState represent Codex Naturalis' setup phase ,the users can choose their private Goal and play their StarterCard
 */
public class InitState extends GameState{
    /**
     * InitState's constructor
     * @param game the game which is being played by the users
     * @param gameManager collection of all the games started but not yet finished
     */
    InitState(Game game, GameManager gameManager) {
        super(gameManager);
        this.game=game;
    }
    /**
     * If all players have chosen their private goal and have played their StarterCard the Turn phase can start, otherwise controller must wait
     * @author Giuseppe Laguardia
     * @return returns to the controller the new state otherwise returns this
     */
    protected GameState nextState(){
        if(game.getPlayers().stream().
                allMatch(p->(HasChosenGoal(p) && HasPlayedStarterCard(p))))
            return new TurnState(game,gameManager);
        return this;
    }

    /**
     * Checks if the player has chosen his public goal
     * @author Giuseppe Laguardia
     * @param p the player subject of the check
     * @return true if the check passes, false otherwise
     */
    private boolean HasPlayedStarterCard(Player p) {
        return p.getField().containsValue(p.getStarterCard());
    }
    /**
     * Checks if the player has played his starterCard
     * @author Giuseppe Laguardia
     * @param p the player subject of the check
     * @return true if the check passes, false otherwise
     */
    private boolean HasChosenGoal(Player p) {
        return p.getGoal() !=null;
    }


    /**
     * @author Riccardo Lapi
     * get the player from userId and set his goal
     * @param userId int that identifies the unique player
     * @param newGoal the chosen goal
     */

    public void ChooseGoal(UUID userId, Goal newGoal) throws InvalidGoalException, InvalidUserId {

        Player player = getPlayerFromUid(userId);

        if(player == null) throw new InvalidUserId();

        if(!Arrays.asList(player.getGoalOptions()).contains(newGoal)) throw new InvalidGoalException();

        player.setGoal(newGoal);
    }
    /**
     * @author Riccardo Lapi
     * set the start card side
     * @param isFront boolean that indicate if the card is placed with the front facing upwards
     */
    public void ChooseStarterCardSide(boolean isFront, UUID userId) throws InvalidUserId {
        Player player = getPlayerFromUid(userId);
        if(player == null) throw new InvalidUserId();

        StarterCard starterCard=player.getStarterCard();
        starterCard.setIsFront(isFront);
        game.playStarterCardFront(isFront);

    }

    private void playStarterCard(UUID userId) {
    }

}
