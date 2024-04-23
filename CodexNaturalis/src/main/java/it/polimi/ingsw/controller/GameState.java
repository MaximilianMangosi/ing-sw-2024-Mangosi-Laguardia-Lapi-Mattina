package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;

public abstract class GameState{
    private Game game;
    protected HashMap<Integer, Player> userIDs;
    private GameManager gameManager;
    GameState(Game game,GameManager gameManager){
        this.game=game;
        this.gameManager=gameManager;

    }


}
