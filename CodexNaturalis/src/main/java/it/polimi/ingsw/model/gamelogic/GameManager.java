package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.App;
import it.polimi.ingsw.model.gamecards.GameBox;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Map<String,Game> gameInProcess= new HashMap<>();
    private Game gameWaiting;
    private Map<String,Integer> playerToGame=new HashMap<>();

    public GameManager() {
    }

    /**
     * creates a new game
     * @author Giuseppe Lagurdia
     * @param numOfPlayers the number of player that can join the game, if there is already a game waiting for player this parameter is ignored
     * @param newPlayer the Player object joining/creating a game
     * @throws UnacceptableNumberOfPlayersException if numOfPlayer less than 2
     * @throws PlayerNameNotUniqueException if any Players's name in gameWaiting matches with playerName
     */
    public boolean bootGame(int numOfPlayers, Player newPlayer) throws UnacceptableNumberOfPlayersException, PlayerNameNotUniqueException {


        if(gameWaiting==null){
            GameBox gamebox = new GameBox();
            if(numOfPlayers<2 || numOfPlayers>4)
                throw new UnacceptableNumberOfPlayersException();
            gameWaiting = new Game(newPlayer,numOfPlayers,gamebox);

        }
        else{
            if(!isPlayerNameUnique(newPlayer.getName()))
                throw new PlayerNameNotUniqueException();
            gameWaiting.addPlayer(newPlayer);

        }
        playerToGame.put(newPlayer.getName(),gameWaiting.hashCode());
        if(gameWaiting.getPlayers().size()==gameWaiting.getNumOfPlayers()){
            gameInProcess.put(String.valueOf(gameWaiting.hashCode()),gameWaiting);
            return true;
        }
        return false;
    }

    private boolean isPlayerNameUnique(String playerName) {
        return gameWaiting.getPlayers().stream().anyMatch(p -> p.getName().equals(playerName));
    }

}
