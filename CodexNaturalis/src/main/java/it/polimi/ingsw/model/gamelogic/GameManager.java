package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.gamecards.GameBox;
import it.polimi.ingsw.model.gamelogic.exceptions.InvalidGameID;
import it.polimi.ingsw.model.gamelogic.exceptions.PlayerNameNotUniqueException;
import it.polimi.ingsw.model.gamelogic.exceptions.UnacceptableNumOfPlayersException;

import java.util.*;

public class GameManager {
    private Map<UUID,Game> gameInProcess= new HashMap<>();
    private Map<UUID,Game> gamesWaiting=new HashMap<>();
    private GameBox gameBox;
    private final Map<String, UUID> playerToGame=new HashMap<>();

    public GameManager() {
    }
    public Game getGameWaiting(UUID gameID) throws InvalidGameID {
        return Optional.ofNullable(gamesWaiting.get(gameID)).orElseThrow(InvalidGameID::new);
    }
    public void addGameWaiting(UUID gameID,Game game){
        gamesWaiting.put(gameID,game);
    }
    public GameBox getGameBox() {
        return gameBox;
    }

    public void setGameBox(GameBox gameBox) {
        this.gameBox = gameBox;
    }

    /**
     * creates a new game if there aren't any pending, otherwise joins the pending one
     * @author Giuseppe Lagurdia
     * @param numOfPlayers the number of player that can join the game, if there is already a game waiting for player this parameter is ignored
     * @param newPlayer the Player object joining/creating a game
     * @return the game's id
     * @throws UnacceptableNumOfPlayersException if numOfPlayer less than 2 or more than 4
     */
    public UUID bootGame(int numOfPlayers, Player newPlayer) throws UnacceptableNumOfPlayersException, PlayerNameNotUniqueException {
        if(numOfPlayers<2 || numOfPlayers>4)
            throw new UnacceptableNumOfPlayersException();
        if(isPlayerNameTaken(newPlayer.getName()))
            throw  new PlayerNameNotUniqueException();
        UUID gameID=UUID.randomUUID();
        gamesWaiting.put(gameID,new Game(newPlayer,numOfPlayers,gameBox));
        playerToGame.put(newPlayer.getName(),gameID);
        return gameID;

    }

    private boolean isPlayerNameTaken(String playerName) {
        return playerToGame.keySet().stream().anyMatch(p -> p.equals(playerName));
    }

    /**
     * @author Riccardo Lapi
     * remove the given game from  gameInProcess
     * @param game the game to be removed
     */
    public void deleteGame(Game game){
        gameInProcess.entrySet().removeIf(e->e.getValue().equals(game));
    }

    /**
     * @author Riccardo Lapi
     * remove the given player from the PlayersToGame
     * @param nickName String that identifies the Player
     */
    public void deletePlayerFromPlayersToGame(String nickName){
        playerToGame.remove(nickName);
    }

    public boolean joinGame(UUID gameID,Player newPlayer) throws PlayerNameNotUniqueException, InvalidGameID {

        if(isPlayerNameTaken(newPlayer.getName()))
            throw new PlayerNameNotUniqueException();

        Game game = gamesWaiting.get(gameID);
        game.addPlayer(newPlayer);
        playerToGame.put(newPlayer.getName(),gameID);

        if(game.getPlayers().size()==game.getNumOfPlayers()){
            gameInProcess.put(gameID,game);
            return true;
        }
        return false;
    }
}
