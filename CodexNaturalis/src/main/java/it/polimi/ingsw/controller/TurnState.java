package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinates;
import it.polimi.ingsw.model.gamecards.AllDeckEmptyExeption;
import it.polimi.ingsw.model.gamecards.HandFullException;
import it.polimi.ingsw.model.gamecards.RequirementsNotMetException;
import it.polimi.ingsw.model.gamecards.cards.*;
import it.polimi.ingsw.model.gamelogic.Game;
import it.polimi.ingsw.model.gamelogic.GameManager;

public class TurnState extends GameState{
    TurnState(Game game, GameManager gameManager) {
        super(game, gameManager);
    }

    /**
     * @author Giorgio Mattina
     * checks for Turn rights, and calls playCardFront
     * @param selectedCard
     * @param position
     * @param userId
     * @throws IsNotYourTurnException
     * @throws RequirementsNotMetException
     */
    public void playCardFront(Card selectedCard, Coordinates position, Integer userId) throws IsNotYourTurnException, RequirementsNotMetException {
        //checks if it's the player's turn
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }

        //checks if the player's hand is full
        game.playCardFront(selectedCard,position);

    }

    /**
     * @author Giorgio Mattina
     * checks for Turn rights and calls
     * @param userId
     * @param choice
     * @throws IsNotYourTurnException
     * @throws HandFullException
     */
    public void drawFromDeck(Integer userId,int choice) throws IsNotYourTurnException, HandFullException, AllDeckEmptyExeption {


        if(game.isAreBothDeckEmpty()){
            throw new AllDeckEmptyExeption();
        }

        if(choice == 0 && game.getResourceCardDeck().isEmpty()){
            throw new AllDeckEmptyExeption();
            //Da capire come fare per notificare che un solo mazzo è vuoto
        } else if (choice!=0 && game.getGoldCardDeck().isEmpty()) {
            throw new AllDeckEmptyExeption();

        }

        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        game.drawFromDeck(choice);
    }

    /**
     * @author Giorgio Mattina
     * checks if the decks are empty
     * @param userId
     * @param choice
     * @throws IsNotYourTurnException
     * @throws AllDeckEmptyExeption
     * @throws HandFullException
     */
    public void drawVisibleCard (Integer userId,int choice) throws IsNotYourTurnException, AllDeckEmptyExeption, HandFullException {
        if(game.isAreBothDeckEmpty()){
            throw new AllDeckEmptyExeption();
        }
        if (!userIDs.get(userId).equals(game.getCurrentPlayer())){
            throw new IsNotYourTurnException();
        }
        game.drawVisibleCard(choice);
    }




    //TODO playCards Giorgio
    //todo drawCards Giorgio
    //isFinalTurn() return
}
