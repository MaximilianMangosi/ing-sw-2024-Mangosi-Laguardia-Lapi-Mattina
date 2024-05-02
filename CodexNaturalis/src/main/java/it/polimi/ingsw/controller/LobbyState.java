package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.NoGameExistsException;
import it.polimi.ingsw.model.gamelogic.exceptions.OnlyOneGameException;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.util.UUID;

public class LobbyState extends GameState{
    /**
     * InitState's constructor
     * @param gameManager collection of all the games started but not yet finished, containing the method for boot a game
     */
    public LobbyState(GameManager gameManager){
        super(gameManager) ;
    }
    /**
     * generates a unique UUID object for the new plauer, calls bootGame
     * @author Giorgio Mattina
     * @param numOfPlayers number of players
     * @param playerName the nickname chosen by the player
     * @return the userId for identification
     * @throws UnacceptableNumOfPlayersException
     * @throws PlayerNameNotUniqueException
     */
    public  UUID BootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException, OnlyOneGameException {

        UUID identity = UUID.randomUUID();
        Player newPlayer = new Player(playerName);
        gameManager.bootGame(numOfPlayers,newPlayer);
        userIDs.put(identity,newPlayer);

        return identity;

    }

    /**
     * If a game is not null, meaning is ready to start , returns to the controller the new state otherwise returns this
     * @author Giuseppe Laguardia
     * @return the next GameState
     */
    public GameState nextState(){
        //if game is null means that not enough users joined the game
       if(game!=null)
           return new InitState( game, gameManager,userIDs);
       return this;
    }

    public UUID joinGame(String playerName) throws NoGameExistsException, PlayerNameNotUniqueException {
        UUID identity = UUID.randomUUID();
        Player newPlayer = new Player(playerName);
        boolean isGameFull=gameManager.joinGame(newPlayer);
        userIDs.put(identity,newPlayer);
        if(isGameFull){
            game=gameManager.getGameWaiting();
            gameManager.setGameWaiting(null);// gameWaiting must be null to host multiple game on the server
            game.startGame();
        }
        return identity;
    }
}
