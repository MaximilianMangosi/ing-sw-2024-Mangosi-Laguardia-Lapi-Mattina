package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalOperationException;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.exceptions.*;

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
     *
     * @param numOfPlayers number of players
     * @param playerName   the nickname chosen by the player
     * @return the userId for identification
     * @throws UnacceptableNumOfPlayersException
     * @author Giorgio Mattina
     */
    public GameKey bootGame(int numOfPlayers, String playerName) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException {
        UUID identity = UUID.randomUUID();
        Player newPlayer = new Player(playerName);
        UUID gameID= gameManager.bootGame(numOfPlayers,newPlayer);
        userIDs.put(identity,newPlayer);

        return new GameKey(gameID, identity);

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

    /**
     * @author Giorgio Mattina
     * add the new player to the game, and create a new unique userID
     * @param playerName the username of the player
     * @return the created User id
     * @throws PlayerNameNotUniqueException if the username (playerName) is already used
     */
    public UUID joinGame(UUID gameID,String playerName) throws  PlayerNameNotUniqueException, InvalidGameID {
        UUID identity = UUID.randomUUID();
        Player newPlayer = new Player(playerName);
        boolean isGameFull=gameManager.joinGame(gameID,newPlayer);
        userIDs.put(identity,newPlayer);
        if(isGameFull){
            game=gameManager.getGameWaiting(gameID);
            game.startGame();
        }
        return identity;
    }

    @Override
    public void addToGlobalChat(String message) throws IllegalOperationException {
        throw new IllegalOperationException("write message");
    }

    @Override
    public void addMessage(String receiver, String message, UUID userID) throws IllegalOperationException {
        throw new IllegalOperationException("write message");
    }
}
