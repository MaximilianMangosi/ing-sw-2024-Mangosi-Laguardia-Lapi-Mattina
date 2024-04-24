package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.UnacceptableNumberOfPlayersException;

public class InitState extends GameState{

    InitState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }
    public void BootGame(int numOfPlayers, String playerName) throws UnacceptableNumberOfPlayersException, PlayerNameNotUniqueException{


        gameManager.bootGame(numOfPlayers,playerName);
    }
    /**
     * @author Riccardo Lapu
     * get the player from userId and set his goal
     * @param userId int that identifies the unique player
     * @param newGoal the chosen goal
     */
    public void ChooseGoal(int userId, Goal newGoal) throws InvalidGoalException, InvalidUserId {
        Player player = getUserIDs().get(userId);

        if(player == null) throw new InvalidUserId();

        if(!Arrays.asList(player.getGoalOptions()).contains(newGoal)) throw new InvalidGoalException();

        player.setGoal(newGoal);
    }
    //TODO BootGame Giorgio
    //TODO StartGame
    //todo ChooseGoal(UserId,Goal) Ric
    //todo ChooseStarterCardSide(boolean) Ric

}
