package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

public class TerminalState extends GameState{
    TerminalState(Game game, GameManager gameManager) {
        super( gameManager);
        this.game=game;
    }

    /**
     * @author Giuseppe Laguardia
     * @return null, there is no nextState after TerminalState
     */
    @Override
    protected GameState nextState() {
        return null;
    }

    /**
     * In TerminalState the game is ended (i.e. there is a winner ) so return true
     * @return true
     */
    @Override
    public boolean isGameEnded() {
        return true;
    }
}
