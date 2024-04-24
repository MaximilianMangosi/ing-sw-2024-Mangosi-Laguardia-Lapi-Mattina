package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamecards.goals.Goal;
import it.polimi.ingsw.model.gamelogic.*;

import java.util.UUID;

public class InitState extends GameState{

    InitState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }

    /**
     * generates a unique UUID object for the new plauer, calls bootGame
     * @author Giorgio Mattina
     * @param numOfPlayers number of players
     * @param playerName the nickname chosen by the player
     * @return the userId for identification
     * @throws UnacceptableNumberOfPlayersException
     * @throws PlayerNameNotUniqueException
     */
    public synchronized UUID  BootGame(int numOfPlayers, String playerName) throws UnacceptableNumberOfPlayersException, PlayerNameNotUniqueException{

        UUID identity = UUID.randomUUID();
        //
        Player newPlayer = new Player(playerName);

        gameManager.bootGame(numOfPlayers,newPlayer);

        userIDs.put(identity,newPlayer);

        return identity;

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
