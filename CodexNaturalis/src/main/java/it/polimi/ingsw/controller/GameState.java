package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;
import it.polimi.ingsw.model.gamelogic.Player;

import java.util.HashMap;

public abstract class GameState{
    protected Game game;

    protected HashMap<Integer, Player> userIDs= new HashMap<>();
    protected GameManager gameManager;
    GameState(Game game,GameManager gameManager){
        this.game=game;
        this.gameManager=gameManager;

    }
    public HashMap<Integer, Player> getUserIDs() {
        return userIDs;
    }

}
