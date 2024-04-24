package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public class Controller {
    GameState currentState;

    /**
     * constructor of Controller, creates a new GameState
     * @author Giorgio Mattina
     * @param game
     * @param gameManager
     */
    public Controller(Game game, GameManager gameManager){
        currentState=new InitState(game,gameManager);
    }
    //TODO ChangeState
    //init->turn check tutti i goal settati
    //turn->final dopo drawCard check (areBothEmpty or hasSomeone20points) and  current player is first of the list
    //pepo,giorgio,max,ric
    //final->Terminal compute public goal and getWinner
    //currentState=new FinalTurn(game,gameManager);
}
