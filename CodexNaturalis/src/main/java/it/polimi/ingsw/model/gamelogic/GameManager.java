package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.App;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Map<String,Game> gameInProcess= new HashMap<>();
    private Game gameWaiting;
    private Map<String,Game> playerToGame=new HashMap<>();

    public GameManager() {
    }

    public void bootGame(int numOfPlayer, String playerName){
        Player player= new Player(playerName);
        if(gameWaiting==null){
            gameWaiting = new Game(player,numOfPlayer);
        }
        else{
            gameWaiting.addPlayer(player);
        }
        playerToGame.put(playerName,gameWaiting);

    }

}
