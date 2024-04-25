package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Map<String,Game> gameInProcess= new HashMap<>();
    private Game gameWaiting;
    private GameBox gameBox;
    private Map<String,Integer> playerToGame=new HashMap<>();

    public GameManager() {
    }
    public Game getGameWaiting(){
        return gameWaiting;
    }
    public void setGameWaiting(Game game){
        gameWaiting=game;
    }
    public GameBox getGameBox() {
        return gameBox;
    }

    public void setGameBox(GameBox gameBox) {
        this.gameBox = gameBox;
    }

    /**
     * creates a new game if there aren't any pending otherwise joins the pending one
     * @author Giuseppe Lagurdia
     * @param numOfPlayers the number of player that can join the game, if there is already a game waiting for player this parameter is ignored
     * @param newPlayer the Player object joining/creating a game
     * @throws UnacceptableNumOfPlayersException if numOfPlayer less than 2
     * @throws PlayerNameNotUniqueException if any Players's name in gameWaiting matches with playerName
     */
    public boolean bootGame(int numOfPlayers, Player newPlayer) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException {
        if(gameWaiting==null){
            GameBox gamebox = new GameBox();
            if(numOfPlayers<2 || numOfPlayers>4)
                throw new UnacceptableNumOfPlayersException();
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

    /**
     * @author Riccardo Lapi
     * remove the given game from the gameInProcess
     * @param gameId the gameId
     */
    public void deleteGame(String gameId){
        gameInProcess.remove(gameId);
    }

    /**
     * @author Riccardo Lapi
     * remove the given player from the PlayersToGame
     * @param nickName String that identifies the Player
     */
    public void deletePlayerFromPlayersToGame(String nickName){
        playerToGame.remove(nickName);
    }

}
