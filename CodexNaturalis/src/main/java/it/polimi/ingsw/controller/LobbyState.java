package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.UnacceptableNumberOfPlayersException;

import java.util.UUID;

public class LobbyState extends GameState{
    public LobbyState(GameManager gameManager){
        super(gameManager);
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
    public synchronized UUID BootGame(int numOfPlayers, String playerName) throws UnacceptableNumberOfPlayersException, PlayerNameNotUniqueException{

        UUID identity = UUID.randomUUID();
        //
        Player newPlayer = new Player(playerName);

        gameManager.bootGame(numOfPlayers,newPlayer);

        userIDs.put(identity,newPlayer);

        return identity;

    }

}
