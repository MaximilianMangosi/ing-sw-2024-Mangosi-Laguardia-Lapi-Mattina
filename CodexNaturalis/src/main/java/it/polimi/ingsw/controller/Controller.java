package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.Card;
import it.polimi.ingsw.model.gamelogic.GameManager;

import java.util.UUID;

public class Controller {
    GameState currentState;
    /**
     * constructor of Controller, creates a new GameState
     * @author Giorgio Mattina
     * @param gameManager
     */
    public Controller( GameManager gameManager){
        currentState=new LobbyState(gameManager);
    }

    /**
     * Makes the transition to nextState
     * @author Giuseppe Laguardia
     */
    private synchronized void changeState(){
        currentState=currentState.nextState();
    }
    public synchronized void playCardFront(Card selectedCard, Coordinates position, UUID userId) throws IsNotYourTurnException, RequirementsNotMetException, IllegalPositionException, InvalidCardException, HandNotFullException, IllegalOperationException {
        currentState.playCardFront(selectedCard,position,userId);
        //TODO update view

    }
    //turn->final dopo drawCard check (areBothEmpty or hasSomeone20points) and  current player is first of the list
    //pepo,giorgio,max,ric
    //final->Terminal compute public goal and getWinner
    //currentState=new FinalTurn(game,gameManager);
}
