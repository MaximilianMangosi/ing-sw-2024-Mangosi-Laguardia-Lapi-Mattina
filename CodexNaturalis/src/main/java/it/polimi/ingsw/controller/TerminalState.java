package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

public class TerminalState extends GameState{
    TerminalState(Game game, GameManager gameManager) {
        super( gameManager);
        this.game=game;
    }

    @Override
    protected GameState nextState() {
        return null;
    }

    /**
     * @author Riccardo Lapi
     * remove the current game from the GameManager gameInProcess Map,
     * and remove ecah player in the current game from the GameManager playerToGame Map
     */
    public void deleteGameFromGameManager(){
        String gameHash = String.valueOf(game.hashCode());
        gameManager.deleteGame(gameHash);

        for(Player player : game.getPlayers()){
            gameManager.deletePlayerFromPlayersToGame(player.getName());
        }

    }
}
