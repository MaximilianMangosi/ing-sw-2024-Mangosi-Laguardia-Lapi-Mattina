package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.App;
import it.polimi.ingsw.model.gamecards.GameBox;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Map<String,Game> gameInProcess= new HashMap<>();
    private Game gameWaiting;
    private Map<String,String> playerToGame=new HashMap<>();

    public GameManager() {
    }

    /**
     * creates a new game
     * @author Giuseppe Lagurdia
     * @param numOfPlayer the number of player that can join the game, if there is already a game waiting for player this parameter is ignored
     * @param playerName the unique nickname of the player joining/creating the game
     * @throws LessThanTwoPlayersException if numOfPlayer less than 2
     */
    public void bootGame(int numOfPlayer, String playerName) throws LessThanTwoPlayersException{
        Player player= new Player(playerName);
        GameBox gamebox = new GameBox();
        if(gameWaiting==null){
            gameWaiting = new Game(player,numOfPlayer,gamebox);
        }
        else{
            gameWaiting.addPlayer(player);
        }
        playerToGame.put(playerName,gameWaiting.toString());
        if(gameWaiting.getNumOfPlayers() == gameWaiting.getPlayers().size()){
            gameInProcess.put(gameWaiting.toString(),gameWaiting);
            //gameWaiting.startGame();
        }
    }

}
