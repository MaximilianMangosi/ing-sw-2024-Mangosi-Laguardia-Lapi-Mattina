package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public class TerminalState extends GameState{
    TerminalState(Game game, GameManager gameManager) {
        super( gameManager);
        this.game=game;
    }

    @Override
    protected GameState nextState() {
        return null;
    }
}
