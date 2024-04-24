package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.*;

import java.util.UUID;

public class InitState extends GameState{

    InitState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }




    /**
     * @author Riccardo Lapi
     * get the player from userId and set his goal
     * @param userId int that identifies the unique player
     * @param newGoal the chosen goal
     */

    public void ChooseGoal(UUID userId, Goal newGoal) throws InvalidGoalException, InvalidUserId {

        Player player = getUserIDs().get(userId);

        if(player == null) throw new InvalidUserId();

        if(!Arrays.asList(player.getGoalOptions()).contains(newGoal)) throw new InvalidGoalException();

        player.setGoal(newGoal);
    }
    //TODO StartGame
    //todo ChooseGoal(UserId,Goal) Ric
    //todo ChooseStarterCardSide(boolean) Ric

}
