package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public abstract class GameState {
    private Game game;
    private GameManager gameManager;
    GameState(Game game,GameManager gameManager){
        this.game=game;
        this.gameManager=gameManager;

    }
}
